package com.mytransformation.calories.exceptions

class FoodNotFoundException(val missingIds: List<String>) : RuntimeException(
    "The following ids are missing: ${missingIds.joinToString()}"
)
