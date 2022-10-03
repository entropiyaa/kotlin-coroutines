package com.epam.mentoring.coroutines.repository.impl

import com.epam.mentoring.coroutines.entity.BreedEntity
import com.epam.mentoring.coroutines.repository.CustomBreedRepository
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitOneOrNull
import org.springframework.stereotype.Repository
import java.util.function.*

@Repository
class CustomBreedRepositoryImpl(private val databaseClient: DatabaseClient) : CustomBreedRepository {

    val MAPPING_FUNCTION: BiFunction<Row, RowMetadata, BreedEntity> =
        BiFunction<Row, RowMetadata, BreedEntity> { row: Row, _: RowMetadata? ->
            BreedEntity(
                row["id", Any::class.java] as Long,
                row["breed_name", String::class.java]!!,
                row["image_url", String::class.java]
            )
        }

    override suspend fun findBreedByName(breed: String): BreedEntity? {
        return databaseClient.sql("select * from dogs.breed WHERE breed_name = $1")
            .bind(0, breed)
            .map(MAPPING_FUNCTION)
            .awaitOneOrNull()
    }
}