package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

class UserMeals (
    var breakfast: Meal? = null,
    var lunch: Meal? = null,
    var dinner: Meal? = null,
    var snacks: Meal? = null,
) {

    class Meal(
        var totalCalorie: Int? = null,
        val totalCarbohydrate: Int? = null,
        val totalFat: Int? = null,
        val totalProtein: Int? = null,
        val contents: ArrayList<Map<String,String>>?
    )
}
