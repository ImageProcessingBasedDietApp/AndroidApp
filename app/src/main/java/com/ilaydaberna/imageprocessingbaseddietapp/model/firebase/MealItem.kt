package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

data class MealItem(
        var totalCalorie: Int = 0,
        val totalCarbohydrate: Int = 0,
        val totalFat: Int = 0,
        val totalProtein: Int = 0,
        val type: String = "",
        val contents: ArrayList<Food?>?
)