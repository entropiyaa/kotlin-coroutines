package com.epam.mentoring.coroutines.exception

import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.result.view.ViewResolver
import reactor.core.publisher.Mono
import java.util.stream.*


@Configuration
@Order(-2)
@Component
class ApiErrorWebExceptionHandler @Autowired constructor(
    errorAttributes: ErrorAttributes?,
    applicationContext: ApplicationContext?,
    viewResolvers: ObjectProvider<ViewResolver>,
    serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(errorAttributes, WebProperties.Resources(), applicationContext) {

    init {
        this.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()))
        this.setMessageWriters(serverCodecConfigurer.writers)
        this.setMessageReaders(serverCodecConfigurer.readers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse?>? {
        return RouterFunctions.route(
            RequestPredicates.all()
        ) { renderErrorResponse(it) }
    }

    private fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse?> {
        val errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.defaults())
        val throwable = getError(request)
        var status = HttpStatus.INTERNAL_SERVER_ERROR
        if (throwable is ApiException) {
            status = throwable.httpStatus
            errorAttributes["status"] = status
            errorAttributes["error"] = throwable.error
        } else {
            errorAttributes["error"] = Error(throwable.message)
        }
        return ServerResponse.status(status)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(errorAttributes))
    }
}