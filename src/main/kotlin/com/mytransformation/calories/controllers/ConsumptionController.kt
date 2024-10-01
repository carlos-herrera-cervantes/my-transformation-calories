package com.mytransformation.calories.controllers

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.mytransformation.calories.config.Api
import com.mytransformation.calories.models.*
import com.mytransformation.calories.repositories.*

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

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val moment: String = consumptionCreation.moment.format(formatter)
        val consumptionSum = consumptionResultRepository.findByDate(userId, moment)
        val consumption = food.calculateConsumption(userId, consumptionCreation)

        if (consumptionSum == null) {
            consumptionResultRepository.insert(ConsumptionResult(
                userId = userId,
                calories = consumption.calories,
                protein = consumption.protein,
                carbs = consumption.carbs,
                fats = consumption.fats,
                moment = moment,
                createdAt = consumptionCreation.moment
            ))
        } else {
            consumptionSum.calories += consumption.calories
            consumptionSum.protein += consumption.protein
            consumptionSum.carbs += consumption.carbs
            consumptionSum.fats += consumption.fats
            consumptionSum.updatedAt = LocalDateTime.now()
            consumptionResultRepository.save(consumptionSum)
        }

        val creation: Consumption = consumptionRepository.insert(consumption)

        return ResponseEntity(creation, HttpStatus.CREATED)
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
            it.updatedAt = LocalDateTime.now()
            consumptionResultRepository.save(it)
        }

        consumptionRepository.delete(queryResult)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
