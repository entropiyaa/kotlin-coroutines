package com.epam.mentoring.coroutines.client.model

data class BreedResponse(val message: Map<String, List<String>>,
                         val status: String)