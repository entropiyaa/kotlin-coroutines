package com.epam.mentoring.coroutines.entity

import com.epam.mentoring.coroutines.model.BreedDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "breed", schema = "dogs")
data class BreedEntity(
    @Id val id: Long? = null,
    @Column("breed_name") val breedName: String,
    @Column("image_url") var imageUrl: String? = null
) {
    companion object {
        fun toDomain(breedEntity: BreedEntity, types: List<String>?) = BreedDto(
            breed = breedEntity.breedName,
            imageUrl = breedEntity.imageUrl,
            types = types
        )
    }
}