package com.mytransformation.calories.models

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.bson.types.ObjectId

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.*
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Document("foods")
data class Food(
    @Id
    val id: String = ObjectId().toHexString(),

    @Field("name")
    val name: String = "",

    @Field("measurement_unit")
    val measurementUnit: String = "",

    @Field("calories")
    val calories: Double = 0.0,

    @Field("protein")
    val protein: Double = 0.0,

    @Field("carbs")
    val carbs: Double = 0.0,

    @Field("fats")
    val fats: Double = 0.0,

    @Field("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Field("updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
