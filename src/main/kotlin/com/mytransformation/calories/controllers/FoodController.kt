package com.mytransformation.calories.controllers

import com.mytransformation.calories.config.*
import com.mytransformation.calories.models.*
import com.mytransformation.calories.repositories.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("${Api.PATH}/v1/foods")
class FoodController @Autowired constructor(private val foodRepository: FoodRepository) {
    @GetMapping
    fun getAll(): ResponseEntity<List<Food>> {
        val foods: List<Food> = foodRepository.findAll()
        return ResponseEntity(foods, HttpStatus.OK)
    }

    @PostMapping
    fun create(@RequestBody food: Food): ResponseEntity<Food> {
        val creation: Food = foodRepository.insert(food)
        return ResponseEntity(creation, HttpStatus.CREATED)
    }

    @PutMapping("{id}")
    fun update(@PathVariable("id") id: String, @RequestBody food: Food): ResponseEntity<Food> {
        food.updatedAt = LocalDateTime.now()

        val update: Food = foodRepository.findById(id).let {
            foodRepository.save(food)
        }

        return ResponseEntity(update, HttpStatus.OK)
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: String): ResponseEntity<HttpStatus> {
        foodRepository.deleteById(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
