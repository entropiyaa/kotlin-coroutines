package com.epam.mentoring.coroutines.controller

import com.epam.mentoring.coroutines.handler.BreedHandler
import com.epam.mentoring.coroutines.model.BreedDto
import com.epam.mentoring.coroutines.service.BreedService
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/*
* With coroutines and reactor project you can just mark controller methods as suspend functions
* or you can return Kotlin's Flow and it will work asynchronously
* */
@RestController
@RequestMapping("/api/v1/breeds")
class BreedController(
    private val breedService: BreedService
) {

    @GetMapping
    fun getBreeds(): Flow<BreedDto> = breedService.getBreeds()

    @GetMapping("/{breed_type}/image")
    suspend fun getBreedImage(@PathVariable("breed_type") breedType: String) = breedService.getImage(breedType)
}
