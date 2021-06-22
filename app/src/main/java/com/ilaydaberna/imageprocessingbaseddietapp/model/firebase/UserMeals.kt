package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField

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

    companion object{
        fun getEmptyMeals() = Meal(
            0,
            0,
            0,
            0,
            arrayListOf()
        )

        fun getMeals(document: DocumentSnapshot) = Meal (
                document.getField<Int>("totalCalorie"),
                document.getField<Int>("totalCarbohydrate"),
                document.getField<Int>("totalFat"),
                document.getField<Int>("totalProtein"),
                document["contents"] as ArrayList<Map<String, String>>?
        )
    }
}
