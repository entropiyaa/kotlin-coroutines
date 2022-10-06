package com.epam.mentoring.coroutines.exception

import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/* Simple controller advice will work with coroutines reactive. */
@RestControllerAdvice
class ApiErrorWebExceptionHandler {

    @ExceptionHandler(ApiException::class)
    fun handleApiException(exception: ApiException): ResponseEntity<Error> {
        return ResponseEntity.status(exception.httpStatus)
            .body(exception.error)
    }

    @ExceptionHandler(Throwable::class)
    fun defaultHandler(exception: Throwable): ResponseEntity<Error> {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
            .body(Error(exception.message ?: exception::class.java.simpleName))
    }
}
