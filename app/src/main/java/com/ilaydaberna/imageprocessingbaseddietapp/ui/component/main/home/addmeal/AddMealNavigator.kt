package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home.addmeal

import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.Food

interface AddMealNavigator {
    fun removeUserFood(userFood: Food, amount: Double)
}