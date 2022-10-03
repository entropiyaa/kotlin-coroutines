package com.epam.mentoring.coroutines.repository

import com.epam.mentoring.coroutines.entity.BreedEntity

interface CustomBreedRepository {
    suspend fun findBreedByName(breed: String): BreedEntity?
}