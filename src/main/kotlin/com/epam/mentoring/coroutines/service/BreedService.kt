package com.epam.mentoring.coroutines.service

import com.epam.mentoring.coroutines.model.BreedDto
import kotlinx.coroutines.flow.Flow

interface BreedService {
    fun getBreeds(): Flow<BreedDto>

    suspend fun getImage(breed: String): String
}