package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

data class Food(
        val id: Int = 0,
        val fileName: String = "",
        val name: String = "",
        val calorie: Double = 0.0,
        val carbohydrate: Double = 0.0,
        val fat: Double = 0.0,
        val protein: Double = 0.0,
        val servingType: ServingType = ServingType(0, "")
)
