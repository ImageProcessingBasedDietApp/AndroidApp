package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

import androidx.databinding.ObservableField

object FoodSingleton {
    var food: ObservableField<ArrayList<Food>> = ObservableField(arrayListOf())
}