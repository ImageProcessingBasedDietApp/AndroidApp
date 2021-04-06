package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

data class Food(
        val id: Int = 0,
        val name: String = "",
        val calorie: Int = 0,
        val carbohydrate: Int = 0,
        val fat: Int = 0,
        val protein: Int = 0,
        val servingType: ServingType = ServingType(0, "")
)