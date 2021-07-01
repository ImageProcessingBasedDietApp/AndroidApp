package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MealItem(
        var totalCalorie: Int = 0,
        val totalCarbohydrate: Int = 0,
        val totalFat: Int = 0,
        val totalProtein: Int = 0,
        val type: String = "",
        val contents: List<Food?>?,
        val amounts: ArrayList<MutableMap<String, String>>?
): Parcelable