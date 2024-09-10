package com.mytransformation.calories.controllers

import com.mytransformation.calories.models.SingleResponse

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("my-transformation-calories/api")
class HealthController {
    @GetMapping("/check")
    fun check(): ResponseEntity<SingleResponse> {
        val singleResponse = SingleResponse()
        singleResponse.message = "Everything is OK"
        return ResponseEntity(singleResponse, HttpStatus.OK)
    }
}
