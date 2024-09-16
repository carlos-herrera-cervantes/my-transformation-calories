package com.mytransformation.calories.models

import java.time.LocalDateTime

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Document("foods")
data class Food(
    @Id
    val id: String = ObjectId().toHexString(),

    @Field("name")
    val name: String = "",

    @Field("measurement_unit")
    val measurementUnit: String = "",

    @Field("portion")
    val portion: Int = 0,

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
) {
    fun calculateConsumption(userId: String, consumptionCreation: ConsumptionCreation): Consumption {
        val caloriesConsumed: Double = (this.calories / this.portion) * consumptionCreation.quantity
        val proteinsConsumed: Double = (this.protein / this.portion) * consumptionCreation.quantity
        val fatsConsumed: Double = (this.fats / this.portion) * consumptionCreation.quantity
        val carbsConsumed: Double = (this.carbs / this.portion) * consumptionCreation.quantity

        return Consumption(
            userId = userId,
            quantity = consumptionCreation.quantity,
            foodId = this.id,
            calories = caloriesConsumed,
            protein = proteinsConsumed,
            carbs = carbsConsumed,
            fats = fatsConsumed,
            moment = consumptionCreation.moment,
            partialFood = PartialFood(
                name = this.name,
                measurementUnit = this.measurementUnit
            )
        )
    }
}
