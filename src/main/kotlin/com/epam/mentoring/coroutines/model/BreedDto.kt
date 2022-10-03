package com.epam.mentoring.coroutines.model

data class BreedDto(
    val breed: String,
    val imageUrl: String? = null,
    val types: List<String>? = emptyList()
)