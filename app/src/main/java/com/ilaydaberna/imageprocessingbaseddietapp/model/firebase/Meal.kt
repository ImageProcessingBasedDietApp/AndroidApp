package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

import com.google.firebase.Timestamp

data class Meal(
        val userID: String = "",
        val contents: ArrayList<Food>,
        val date: Timestamp = Timestamp.now(),
        val totalCalorie: Int = 0,
        val totalCarbohydrate: Int = 0,
        val totalFat: Int = 0,
        val totalProtein: Int = 0,
        val type: MealType = MealType(0, "")
)