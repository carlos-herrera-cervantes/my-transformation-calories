package com.mytransformation.calories.controllers

import java.time.LocalDateTime

import com.mytransformation.calories.config.Api
import com.mytransformation.calories.models.ConsumptionResult
import com.mytransformation.calories.repositories.ConsumptionResultRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("${Api.PATH}/v1/consumptions/result")
class ConsumptionResultController @Autowired constructor(
    private val consumptionResultRepository: ConsumptionResultRepository
) {
    @GetMapping("me")
    fun getAll(
        @RequestParam from: LocalDateTime,
        @RequestParam to: LocalDateTime,
        @RequestHeader("user-id") userId: String
    ): ResponseEntity<List<ConsumptionResult>> {
        val consumptionResult = consumptionResultRepository.findAllMe(userId, from, to)
        return ResponseEntity(consumptionResult, HttpStatus.OK)
    }

    @GetMapping("/current/me")
    fun get(@RequestHeader("user-id") userId: String, @RequestParam date: String): ResponseEntity<ConsumptionResult> {
        val result: ConsumptionResult = consumptionResultRepository.findByDate(userId, date)
            ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @PutMapping("/me/{id}")
    fun update(
        @RequestHeader("user-id") userId: String,
        @PathVariable("id") id: String,
        @RequestBody consumptionResult: ConsumptionResult
    ): ResponseEntity<ConsumptionResult> {
        consumptionResult.updatedAt = LocalDateTime.now()

        val update: ConsumptionResult? = consumptionResultRepository.findMe(id, userId)

        if (update == null) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        } else {
            consumptionResultRepository.save(consumptionResult)
        }

        return ResponseEntity(update, HttpStatus.OK)
    }
}
