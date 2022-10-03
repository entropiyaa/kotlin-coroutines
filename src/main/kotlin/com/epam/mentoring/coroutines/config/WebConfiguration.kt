package com.epam.mentoring.coroutines.config

import com.epam.mentoring.coroutines.handler.BreedHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class WebConfiguration {

    @Bean
    fun routes(breedHandler: BreedHandler) =
        coRouter {
            GET("/api/v1/breeds", breedHandler::getBreeds)
            GET("/api/v1/breeds/{breed_type}/image", breedHandler::getBreedImage)
        }
}