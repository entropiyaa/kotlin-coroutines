package com.epam.mentoring.coroutines.initializer

import com.epam.mentoring.coroutines.client.DogApi
import com.epam.mentoring.coroutines.entity.BreedEntity
import com.epam.mentoring.coroutines.entity.BreedTypesEntity
import com.epam.mentoring.coroutines.repository.BreedRepository
import com.epam.mentoring.coroutines.repository.BreedTypesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class DatabaseInitializer(private val applicationScope: CoroutineScope,
                          private val breedRepository: BreedRepository,
                          private val breedTypesRepository: BreedTypesRepository,
                          private val dogApi: DogApi) {

    @EventListener(classes = [ApplicationReadyEvent::class])
    fun onApplicationReady() {
        applicationScope.launch {
            if (breedRepository.count() == 0L) {
                val breedsMap = dogApi.getBreeds()?.message ?: emptyMap()
                for ((breed, breedType) in breedsMap) {
                    val breedEntity = breedRepository.save(BreedEntity(breedName = breed))
                    breedType.forEach {
                        breedTypesRepository.save(BreedTypesEntity(type = it, breedId = breedEntity.id!!))
                    }
                }
            }
        }
    }
}