package com.epam.mentoring.coroutines.client

import com.epam.mentoring.coroutines.client.model.BreedResponse
import com.epam.mentoring.coroutines.client.model.ImageResponse

interface DogApi {
    suspend fun getImage(breed: String): ImageResponse?

    suspend fun getBreeds(): BreedResponse?
}