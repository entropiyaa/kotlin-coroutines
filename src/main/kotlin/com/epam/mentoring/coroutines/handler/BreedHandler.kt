package com.epam.mentoring.coroutines.handler

import com.epam.mentoring.coroutines.service.BreedService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class BreedHandler(private val breedService: BreedService) {

    suspend fun getBreeds(request: ServerRequest): ServerResponse {
        return ok().bodyAndAwait(breedService.getBreeds())
    }

    suspend fun getBreedImage(request: ServerRequest): ServerResponse {
        return ok().bodyValueAndAwait(breedService.getImage(request.pathVariable("breed_type").lowercase()))
    }
}