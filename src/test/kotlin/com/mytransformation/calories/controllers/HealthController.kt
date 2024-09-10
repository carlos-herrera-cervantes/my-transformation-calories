package com.mytransformation.calories.controllers

import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest
class HealthControllerTest(@Autowired val mockMvc: MockMvc) {
    @Test
    fun check_thenReturnsJsonWithStatus200() {
        mockMvc.perform(get("/my-transformation-calories/api/check"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Everything is OK"))
    }
}
