package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Food(
        @SerializedName("ID")
        val id: Int = 0,
        @SerializedName("fileName")
        val fileName: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("calorie")
        val calorie: Double = 0.0,
        @SerializedName("carbohydrate")
        val carbohydrate: Double = 0.0,
        @SerializedName("fat")
        val fat: Double = 0.0,
        @SerializedName("protein")
        val protein: Double = 0.0,
        @SerializedName("servingType")
        val servingType: String = ""
): Parcelable
