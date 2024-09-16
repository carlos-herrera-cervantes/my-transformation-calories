package com.mytransformation.calories.repositories

import java.time.LocalDateTime

import com.mytransformation.calories.models.Consumption

import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ConsumptionRepository : MongoRepository<Consumption, String> {
    @Aggregation(pipeline = [
        "{ \$match: { user_id: ?0, moment: { \$gte: ?1, \$lte: ?2 } } }",
        "{ \$sort: { moment: -1 } }"
    ])
    fun findAllMe(userId: String, from: LocalDateTime, to: LocalDateTime): List<Consumption>

    @Query("{ 'user_id': ?0, '_id': ?1 }")
    fun findMe(userId: String, id: String): Consumption?
}
