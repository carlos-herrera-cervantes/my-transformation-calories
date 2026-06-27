package com.mytransformation.calories.controllers

import com.mytransformation.calories.models.ConsumptionCreation
import com.mytransformation.calories.repositories.FoodRepository
import com.ninjasquad.springmockk.MockkBean
import com.fasterxml.jackson.databind.ObjectMapper
import com.mytransformation.calories.models.Consumption
import com.mytransformation.calories.models.ConsumptionResult
import com.mytransformation.calories.models.Food
import com.mytransformation.calories.repositories.ConsumptionRepository
import com.mytransformation.calories.repositories.ConsumptionResultRepository

import org.springframework.http.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import io.mockk.every
import io.mockk.verify

import java.time.LocalDateTime

@WebMvcTest(ConsumptionController::class)
class ConsumptionControllerTests {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var foodRepository: FoodRepository

    @MockkBean
    lateinit var consumptionRepository: ConsumptionRepository

    @MockkBean
    lateinit var consumptionResultRepository: ConsumptionResultRepository

    @Test
    fun createMeal_returns400StatusCode_whenMissingFood() {
        every { foodRepository.findAllById(any()) } returns emptyList()

        val requestBody = mutableListOf(
            ConsumptionCreation(
                foodId = "6a401b03e1fb4010f8ca6118",
                quantity = 100.0,
                moment = LocalDateTime.now(),
            ),
        )
        mockMvc.perform(post("/my-transformation-calories/api/v1/consumptions/me/meal")
            .contentType(MediaType.APPLICATION_JSON)
            .header("user-id", "6a4028cce1fb4010f8ca6119")
            .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isNotFound)

        verify(exactly = 1) { foodRepository.findAllById(any()) }
    }

    @Test
    fun createMeal_returns201StatusCode_whenInsertMeal() {
        every { foodRepository.findAllById(any()) } returns mutableListOf(
            Food(
                id = "6a401b03e1fb4010f8ca6118",
                name = "Test Food",
                measurementUnit = "gr",
                portion = 100,
                calories = 120.0,
                protein = 25.0,
                carbs = 0.0,
                fats = 1.0,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            ),
        )
        every { consumptionResultRepository.findByDate(any(), any()) } returns null
        every { consumptionResultRepository.insert(any<ConsumptionResult>()) } returns ConsumptionResult()
        every { consumptionRepository.insert(any<List<Consumption>>()) } returns mutableListOf(
            Consumption(),
        )

        val requestBody = mutableListOf(
            ConsumptionCreation(
                foodId = "6a401b03e1fb4010f8ca6118",
                quantity = 100.0,
                moment = LocalDateTime.now(),
            ),
        )
        mockMvc.perform(post("/my-transformation-calories/api/v1/consumptions/me/meal")
            .contentType(MediaType.APPLICATION_JSON)
            .header("user-id", "6a4028cce1fb4010f8ca6119")
            .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().isCreated)

        verify(exactly = 1) { foodRepository.findAllById(any()) }
        verify(exactly = 1) { consumptionResultRepository.findByDate(any(), any()) }
        verify(exactly = 1) { consumptionResultRepository.insert(any<ConsumptionResult>()) }
        verify(exactly = 1) { consumptionRepository.insert(any<List<Consumption>>()) }
    }
}
