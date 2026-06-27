package com.mytransformation.calories.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler
    fun handleFoodNotFound(ex: FoodNotFoundException): ResponseEntity<Map<String, Any>> {
        val body = mapOf(
            "timestamp" to System.currentTimeMillis(),
            "message" to ex.message.toString(),
        )
        return ResponseEntity(body, HttpStatus.NOT_FOUND)
    }
}
