package com.epam.mentoring.coroutines.service

import com.epam.mentoring.coroutines.client.DogApi
import com.epam.mentoring.coroutines.client.model.ImageResponse
import com.epam.mentoring.coroutines.entity.BreedEntity
import com.epam.mentoring.coroutines.exception.NotFoundException
import com.epam.mentoring.coroutines.model.BreedDto
import com.epam.mentoring.coroutines.repository.BreedRepository
import com.epam.mentoring.coroutines.repository.BreedTypesRepository
import com.epam.mentoring.coroutines.service.impl.BreedServiceImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import reactor.core.publisher.Flux
import kotlin.random.Random

@ExtendWith(MockKExtension::class)
class BreedServiceImplTests {

    @MockK
    lateinit var breedRepository: BreedRepository

    @MockK
    lateinit var breedTypesRepository: BreedTypesRepository

    @MockK
    lateinit var dogApi: DogApi

    lateinit var breedService: BreedService

    @BeforeEach
    fun setUp() {
        breedService = BreedServiceImpl(breedRepository, breedTypesRepository, dogApi)
    }

    @Test
    fun `test get all breeds when repository returns empty flow`() = runBlocking {
        every { breedRepository.findAll() } returns emptyFlow()

        val breeds = breedService.getBreeds()

        assert(breeds.count() == 0)
    }

    @Test
    fun `test get all breeds`() = runBlocking {
        val breedsFlow = Flux.just("akita", "corgi")
            .map { BreedEntity(
                id = Random(100).nextLong(),
                breedName = it,
                imageUrl = "/path/image.jpg") }
            .asFlow()
        val expected = breedsFlow
            .map { BreedDto(breed = it.breedName, imageUrl = it.imageUrl) }
        every { breedRepository.findAll() } returns breedsFlow
        every { breedTypesRepository.findAllTypeByBreedId(any()) } returns emptyFlow()

        val breeds = breedService.getBreeds()

        assert(breeds.count() == 2)
        assert(breeds.toList().containsAll(expected.toList()))
    }

    @Test
    fun `test get all breeds with types`() = runBlocking {
        val breedsFlow = Flux.just("bulldog")
            .map { BreedEntity(
                id = Random(100).nextLong(),
                breedName = it,
                imageUrl = "/path/image.jpg") }
            .asFlow()
        val typesFlow = flowOf("boston", "english", "french")
        val expected = breedsFlow
            .map { BreedDto(breed = it.breedName, imageUrl = it.imageUrl, types = typesFlow.toList()) }
        every { breedRepository.findAll() } returns breedsFlow
        every { breedTypesRepository.findAllTypeByBreedId(any()) } returns typesFlow

        val breeds = breedService.getBreeds()

        assert(breeds.count() == 1)
        assert(breeds.toList().containsAll(expected.toList()))
    }

    @Test
    fun `test get image when image exists in the database`() = runBlocking {
        val breedName = "akita"
        val imageUrl = "/path/image.jpg"
        val breed = BreedEntity(breedName = breedName, imageUrl = imageUrl)
        coEvery { breedRepository.findBreedByName(breedName) } returns breed

        val image = breedService.getImage(breedName)

        assert(image == imageUrl)
    }

    @Test
    fun `test get image when image does not exist in the database`() = runBlocking {
        val breedName = "akita"
        val imageUrl = "/path/image.jpg"
        val breed = BreedEntity(breedName = breedName)
        coEvery { breedRepository.findBreedByName(breedName) } returns breed
        coEvery { dogApi.getImage(breedName) } returns ImageResponse(listOf(imageUrl), "success")
        coEvery { breedRepository.save(any()) } returns breed.copy(imageUrl = imageUrl)

        val image = breedService.getImage(breedName)

        assert(image == imageUrl)
    }

    @Test
    fun `test get image when breed with provided name does not exist`() {
        val breedName = "akita"
        coEvery { breedRepository.findBreedByName(breedName) } returns null

        assertThrows(NotFoundException::class.java) {
            runBlocking { breedService.getImage(breedName) }
        }
    }
}