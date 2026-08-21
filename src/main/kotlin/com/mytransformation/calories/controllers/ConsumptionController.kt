package com.mytransformation.calories.controllers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.mytransformation.calories.config.Api
import com.mytransformation.calories.exceptions.FoodNotFoundException
import com.mytransformation.calories.models.*
import com.mytransformation.calories.repositories.*

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("${Api.PATH}/v1/consumptions")
class ConsumptionController @Autowired constructor(
    private val consumptionRepository: ConsumptionRepository,
    private val foodRepository: FoodRepository,
    private val consumptionResultRepository: ConsumptionResultRepository
) {
    @GetMapping("me")
    fun getAll(
        @RequestParam from: LocalDateTime,
        @RequestParam to: LocalDateTime,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<List<Consumption>> {
        val consumptions: List<Consumption> = consumptionRepository.findAllMe(userId, from, to)
        return ResponseEntity(consumptions, HttpStatus.OK)
    }

    @GetMapping("me/{id}")
    fun get(
        @RequestHeader("user-id") userId: String,
        @PathVariable("id") id: String
    ): ResponseEntity<Consumption> {
        val queryResult: Consumption = consumptionRepository.findMe(userId, id)
            ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        return ResponseEntity(queryResult, HttpStatus.OK)
    }

    @PostMapping("me")
    fun create(
        @RequestHeader("user-id") userId: String,
        @RequestBody consumptionCreation: ConsumptionCreation
    ): ResponseEntity<Consumption> {
        val food = foodRepository.findByIdOrNull(consumptionCreation.foodId)
            ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)

        val consumption = food.calculateConsumption(userId, "", consumptionCreation)
        saveConsumptionResult(consumptionCreation, userId, consumption)
        val creation: Consumption = consumptionRepository.insert(consumption)

        return ResponseEntity(creation, HttpStatus.CREATED)
    }

    @PostMapping("me/meal")
    fun createMeal(
        @RequestHeader("user-id") userId: String,
        @RequestBody consumptionCreation: List<ConsumptionCreation>
    ): ResponseEntity<List<Consumption>> {
        val foodIds = consumptionCreation.map { it.foodId }
        val food = foodRepository.findAllById(foodIds)
        val foundFoodIds = food.map { it.id }.toSet()
        val missingFoods = foodIds.filterNot { it in foundFoodIds }

        if (missingFoods.isNotEmpty() || food.isEmpty()) {
            throw FoodNotFoundException(missingFoods)
        }

        val mealId = ObjectId().toString()
        val meals = food.map { currentFood ->
            val foodConsumptionDetail = consumptionCreation.first { it.foodId == currentFood.id }
            currentFood.calculateConsumption(userId, mealId, foodConsumptionDetail)
        }

        consumptionCreation.forEach { creationData ->
            val creation = meals.first { food -> food.foodId == creationData.foodId }
            saveConsumptionResult(creationData, userId, creation)
        }

        val creations = consumptionRepository.insert(meals)

        return ResponseEntity(creations, HttpStatus.CREATED)
    }

    @DeleteMapping("me/{id}")
    fun delete(
        @RequestHeader("user-id") userId: String,
        @PathVariable("id") id: String
    ): ResponseEntity<HttpStatus> {
        val queryResult: Consumption = consumptionRepository.findMe(userId, id)
            ?: return ResponseEntity(HttpStatus.NO_CONTENT)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val moment: String = queryResult.moment.format(formatter)

        consumptionResultRepository.findByDate(userId, moment)?.let {
            it.calories -= queryResult.calories
            it.protein -= queryResult.protein
            it.carbs -= queryResult.carbs
            it.fats -= queryResult.fats
            it.sugar -= queryResult.sugar
            it.sodium -= queryResult.sodium
            it.calcium -= queryResult.calcium
            it.updatedAt = LocalDateTime.now()
            consumptionResultRepository.save(it)
        }

        consumptionRepository.delete(queryResult)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    private fun saveConsumptionResult(consumptionCreation: ConsumptionCreation, userId: String, consumption: Consumption) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val moment: String = consumptionCreation.moment.format(formatter)
        val consumptionSum = consumptionResultRepository.findByDate(userId, moment)

        if (consumptionSum == null) {
            consumptionResultRepository.insert(ConsumptionResult(
                userId = userId,
                calories = consumption.calories,
                protein = consumption.protein,
                carbs = consumption.carbs,
                fats = consumption.fats,
                sodium = consumption.sodium,
                calcium = consumption.calcium,
                sugar = consumption.sugar,
                moment = moment,
                createdAt = consumptionCreation.moment
            ))
        } else {
            consumptionSum.calories += consumption.calories
            consumptionSum.protein += consumption.protein
            consumptionSum.carbs += consumption.carbs
            consumptionSum.fats += consumption.fats
            consumptionSum.sodium += consumption.sodium
            consumptionSum.calcium += consumption.calcium
            consumptionSum.sugar += consumption.sugar
            consumptionSum.updatedAt = LocalDateTime.now()
            consumptionResultRepository.save(consumptionSum)
        }
    }
}
