package com.epam.mentoring.coroutines.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "breed_types", schema = "dogs")
data class BreedTypesEntity(
    @Id val id: Long? = null,
    @Column("type") val type: String,
    @Column("breed_id") val breedId: Long
)