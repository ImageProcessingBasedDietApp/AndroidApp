package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home.addmeal

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.*
import kotlinx.android.synthetic.main.fragment_add_meal.view.*

class AddMealFragment : Fragment() {

    private var adapterFood = FoodsAdapter()
    private var adapterUserFood = UserFoodAdapter()

    private var foodList = listOf<Food>()
    private var userFoodList = listOf<Food>()

    private var totalCalorie = 0.0
    private var totalProtein = 0.0
    private var totalFat = 0.0
    private var totalCarbohydrate: Double = 0.0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_add_meal, container, false)

        view.rv_food_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.rv_user_food.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        view.rv_food_list.adapter = adapterFood
        view.rv_user_food.adapter = adapterUserFood

        getFoods()

        adapterFood.itemClickListener = {
            addUserFood(it)
            updateTotalView(view)
        }

        adapterUserFood.itemClickListener = {
            removeUserFood(it)
            updateTotalView(view)
        }

        return view
    }

    private fun updateTotalView(view: View){
        view.tv_user_meal_total_calorie.text = totalCalorie.toString() +" kcal"
        view.tv_user_meal_total_protein.text = totalProtein.toString() +" gr"
        view.tv_user_meal_total_carbohydrate.text = totalCarbohydrate.toString() +" gr"
        view.tv_user_meal_total_fat.text = totalFat.toString() +" gr"
    }

    private fun addUserFood(food: Food){
        userFoodList = userFoodList + food
        adapterUserFood.submitList(userFoodList)

        totalCalorie += food.calorie
        totalFat += food.fat
        totalProtein += food.protein
        totalCarbohydrate += food.carbohydrate
    }

    private fun removeUserFood(food: Food){
        userFoodList = userFoodList - food
        adapterUserFood.submitList(userFoodList)

        totalCalorie -= food.calorie
        totalFat -= food.fat
        totalProtein -= food.protein
        totalCarbohydrate -= food.carbohydrate
    }

    private fun getFoods(){
        FirestoreSource().getFoods(object : GetFoodsCallback {
            override fun onCallback(foods: ArrayList<Food>) {
                for (food in foods) {
                    foodList = foodList + food
                    Log.d(ContentValues.TAG,food.name)

                }
                adapterFood.submitList(foodList)
            }
        })
    }
}