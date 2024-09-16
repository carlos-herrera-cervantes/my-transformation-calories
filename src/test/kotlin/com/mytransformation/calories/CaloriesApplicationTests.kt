package com.mytransformation.calories

import io.mockk.mockk

import com.mytransformation.calories.controllers.*
import com.mytransformation.calories.repositories.*

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import kotlin.test.assertNotNull

@SpringBootTest
class CaloriesApplicationTests {
	private final val foodRepository: FoodRepository = mockk()

	private final val consumptionRepository: ConsumptionRepository = mockk()

	private final val consumptionResultRepository: ConsumptionResultRepository = mockk()

	@Autowired
	private val foodController: FoodController = FoodController(foodRepository)

	@Autowired
	private val consumptionController: ConsumptionController = ConsumptionController(consumptionRepository, foodRepository, consumptionResultRepository)

	@Autowired
	private val consumptionResultController: ConsumptionResultController = ConsumptionResultController(consumptionResultRepository)

	@Test
	fun contextLoads() {
		assertNotNull(foodController)
		assertNotNull(consumptionController)
		assertNotNull(consumptionResultController)
	}

}
