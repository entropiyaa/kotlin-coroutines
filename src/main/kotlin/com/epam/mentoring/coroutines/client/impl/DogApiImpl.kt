package com.epam.mentoring.coroutines.client.impl

import com.epam.mentoring.coroutines.client.DogApi
import com.epam.mentoring.coroutines.client.model.BreedResponse
import com.epam.mentoring.coroutines.client.model.ImageResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.util.UriComponentsBuilder

@Component
class DogApiImpl(@Value("\${dog.api.images.url}") private val imageUrl: String,
                 @Value("\${dog.api.breeds.url}") private val breedsUrl: String): DogApi {

    private val BREED_TYPE_PATH_PARAM = "BREED_TYPE"

    private val webClient: WebClient = WebClient.builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .build()

    override suspend fun getImage(breed: String): ImageResponse? {
        var uriComponents = UriComponentsBuilder.fromUriString(imageUrl).build()
        uriComponents = uriComponents.expand(mapOf(BREED_TYPE_PATH_PARAM to breed))
        return webClient.get()
            .uri(uriComponents.toUri())
            .accept(APPLICATION_JSON)
            .retrieve()
            .awaitBodyOrNull()
    }

    override suspend fun getBreeds(): BreedResponse? {
        return webClient.get()
            .uri(breedsUrl)
            .accept(APPLICATION_JSON)
            .retrieve()
            .awaitBodyOrNull()
    }
}