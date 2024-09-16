package com.mytransformation.calories.models

import java.time.LocalDateTime

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Document("consumption_results")
data class ConsumptionResult (
    @Id
    val id: String = ObjectId().toHexString(),

    @Field("user_id")
    val userId: String = "",

    @Field("calories")
    var calories: Double = 0.0,

    @Field("protein")
    var protein: Double = 0.0,

    @Field("carbs")
    var carbs: Double = 0.0,

    @Field("fats")
    var fats: Double = 0.0,

    @Field("moment")
    val moment: String = "",

    @Field("comment")
    val comment: String = "",

    @Field("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Field("updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
