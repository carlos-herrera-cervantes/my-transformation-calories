package com.mytransformation.calories.repositories

import java.time.LocalDateTime

import com.mytransformation.calories.models.ConsumptionResult

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ConsumptionResultRepository : MongoRepository<ConsumptionResult, String> {
    @Query(value = "{ user_id: ?0, moment: ?1 }")
    fun findByDate(userId: String, date: String): ConsumptionResult?

    @Query("{ user_id: ?0, created_at: { \$gte: ?1, \$lte: ?2 } }")
    fun findAllMe(userId: String, from: LocalDateTime, to: LocalDateTime): List<ConsumptionResult>
}
