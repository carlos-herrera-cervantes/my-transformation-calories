package com.mytransformation.calories.models

import java.time.LocalDateTime

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.Document

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Document("consumptions")
data class Consumption(
    @Id
    val id: String = ObjectId().toHexString(),

    @Field("user_id")
    val userId: String = "",

    @Field("quantity")
    val quantity: Int = 0,

    @Field("food_id")
    val foodId: String = "",

    @Field("calories")
    val calories: Double = 0.0,

    @Field("protein")
    val protein: Double = 0.0,

    @Field("carbs")
    val carbs: Double = 0.0,

    @Field("fats")
    val fats: Double = 0.0,

    @Field("moment")
    var moment: LocalDateTime = LocalDateTime.now(),

    @Field("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Field("updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Field("food")
    val partialFood: PartialFood = PartialFood()
)

data class PartialFood(
    @Field("name")
    val name: String = "",

    @Field("measurement_unit")
    val measurementUnit: String = ""
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ConsumptionCreation(
    val foodId: String = "",
    val quantity: Int = 0,
    val moment: LocalDateTime = LocalDateTime.now()
)
