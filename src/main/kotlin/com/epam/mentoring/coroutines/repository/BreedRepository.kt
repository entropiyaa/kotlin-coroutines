package com.epam.mentoring.coroutines.repository

import com.epam.mentoring.coroutines.entity.BreedEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

/*
With CoroutineCrudRepository you can mark method as suspend and just use it like basic CrudRepository
All DB calls will be executed with coroutines
*/
@Repository
interface BreedRepository : CoroutineCrudRepository<BreedEntity, Long> {

    suspend fun findByBreedName(breedName: String): BreedEntity?
}
