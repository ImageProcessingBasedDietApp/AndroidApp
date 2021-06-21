package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home

import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.MealItem

interface HomeNavigator {
    fun openAddMealFragment(mealItem: MealItem)
}