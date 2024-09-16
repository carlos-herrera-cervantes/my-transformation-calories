package com.mytransformation.calories.repositories

import com.mytransformation.calories.models.Food

import org.springframework.data.mongodb.repository.*
import org.springframework.stereotype.Repository

@Repository
interface FoodRepository : MongoRepository<Food, String> {
    @Aggregation(pipeline = [
        "{ '\$sort': { created_at: -1 } }"
    ])
    override fun findAll(): List<Food>
}
