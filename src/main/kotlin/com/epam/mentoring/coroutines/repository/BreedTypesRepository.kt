package com.epam.mentoring.coroutines.repository

import com.epam.mentoring.coroutines.entity.BreedTypesEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param

@Repository
interface BreedTypesRepository: CoroutineCrudRepository<BreedTypesEntity, Long> {

    @Query("SELECT bt.type FROM dogs.breed_types bt WHERE breed_id = :breedId")
    fun findAllTypeByBreedId(@Param("breedId") breedId: Long): Flow<String>
}