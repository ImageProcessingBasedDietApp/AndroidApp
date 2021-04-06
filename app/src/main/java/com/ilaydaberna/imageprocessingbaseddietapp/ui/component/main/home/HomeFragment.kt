package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.Food
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.Meal
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.MealType
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.ServingType
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.rv_meal_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.rv_meal_list.adapter = MealsAdapter(getMeals())

       /* view.layout_breakfast.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addMealFragment)
            Toast.makeText(context, "Kahvaltı", Toast.LENGTH_SHORT).show()
        }
        view.layout_launch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addMealFragment)
            Toast.makeText(context, "Öğle Yemeği", Toast.LENGTH_SHORT).show()
        }
        view.layout_dinner.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addMealFragment)
            Toast.makeText(context, "Akşam Yemeği", Toast.LENGTH_SHORT).show()
        }
        view.layout_snacks.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addMealFragment)
            Toast.makeText(context, "Atıştırmalık", Toast.LENGTH_SHORT).show()
        }*/

        return view
    }

    fun getMeals(): ArrayList<Meal>{
        val meals = arrayListOf<Meal>()
        val foods = arrayListOf<Food>()

        foods.add(0,Food(1, "Tarhana", 105, 10,15,35, ServingType(1, "Kase")))
        foods.add(1,Food(2, "Kuru Fasulye", 155, 10,15,35, ServingType(1, "Tabak")))
        foods.add(2, Food(3, "Pilav", 200, 10,15,35, ServingType(1, "Tabak")))
        meals.add(Meal("123456", foods, Timestamp.now(), 460, 30, 45, 105, MealType(2, "Öğle Yemeği")))
        meals.add(Meal("123456", foods, Timestamp.now(), 460, 30, 45, 105, MealType(3, "Akşam Yemeği")))

        return meals
    }
}