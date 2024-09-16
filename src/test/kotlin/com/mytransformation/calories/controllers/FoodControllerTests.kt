package com.mytransformation.calories.controllers

import io.mockk.*

import com.mytransformation.calories.models.Food
import com.mytransformation.calories.repositories.*
import com.ninjasquad.springmockk.MockkBean

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.data.repository.findByIdOrNull
import org.junit.jupiter.api.Test

@WebMvcTest
class FoodControllerTests(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    lateinit var foodRepository: FoodRepository

    @MockkBean
    lateinit var consumptionRepository: ConsumptionRepository

    @MockkBean
    lateinit var consumptionResultRepository: ConsumptionResultRepository

    @Test
    fun getAll_returns200StatusCode() {
        every { foodRepository.findAll() } returns emptyList()

        mockMvc.perform(get("/my-transformation-calories/api/v1/foods"))
            .andExpect(status().isOk)

        verify(exactly = 1) { foodRepository.findAll() }
    }

    @Test
    fun get_returns404StatusCode() {
        every { foodRepository.findByIdOrNull(any()) } returns null

        mockMvc.perform(get("/my-transformation-calories/api/v1/foods/66e7d3c6263bfca8f1e6cb7d"))
            .andExpect(status().isNotFound)

        verify(exactly = 1) { foodRepository.findByIdOrNull(any()) }
    }

    @Test
    fun get_returns200StatusCode() {
        every { foodRepository.findByIdOrNull(any()) } returns Food(
            name = "Test food"
        )

        mockMvc.perform(get("/my-transformation-calories/api/v1/foods/66e7d3c6263bfca8f1e6cb7d"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test food"))

        verify(exactly = 1) { foodRepository.findByIdOrNull(any()) }
    }
}
