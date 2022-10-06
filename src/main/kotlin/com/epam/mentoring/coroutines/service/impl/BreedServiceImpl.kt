package com.epam.mentoring.coroutines.service.impl

import com.epam.mentoring.coroutines.client.DogApi
import com.epam.mentoring.coroutines.entity.BreedEntity
import com.epam.mentoring.coroutines.exception.NotFoundException
import com.epam.mentoring.coroutines.model.BreedDto
import com.epam.mentoring.coroutines.repository.BreedRepository
import com.epam.mentoring.coroutines.repository.BreedTypesRepository
import com.epam.mentoring.coroutines.service.BreedService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class BreedServiceImpl(private val breedRepository: BreedRepository,
                       private val breedTypesRepository: BreedTypesRepository,
                       private val dogApi: DogApi): BreedService {

    override fun getBreeds(): Flow<BreedDto> {
        return breedRepository.findAll()
            .map { breed ->
                val types =  breedTypesRepository.findAllTypeByBreedId(breed.id!!).toList()
                BreedEntity.toDomain(breed, types)
            }
    }

    override suspend fun getImage(breed: String): String {
        val breedEntity = breedRepository.findByBreedName(breed) ?: throw NotFoundException()
        if (breedEntity.imageUrl.isNullOrEmpty()) {
            val imageUrl = dogApi.getImage(breed)?.message?.get(0)
            breedEntity.imageUrl = imageUrl
            breedRepository.save(breedEntity)
        }
        return breedEntity.imageUrl ?: throw NotFoundException()
    }
}
