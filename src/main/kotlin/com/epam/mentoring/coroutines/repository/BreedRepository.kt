package com.epam.mentoring.coroutines.repository

import com.epam.mentoring.coroutines.entity.BreedEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BreedRepository: CoroutineCrudRepository<BreedEntity, Long>, CustomBreedRepository