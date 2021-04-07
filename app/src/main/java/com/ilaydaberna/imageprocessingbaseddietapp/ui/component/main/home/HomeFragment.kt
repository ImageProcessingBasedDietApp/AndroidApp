package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.futured.donut.DonutSection
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
        view.rv_meal_list.adapter = context?.let { MealsAdapter(it, getMeals(), getFoods()) }

        val sectionCalorie = DonutSection(
            name = "Alınan Kalori",
            color = resources.getColor(R.color.green_500),
            amount = 100f
        )

        view.dv_daily_calorie.cap = 1500f
        view.dv_daily_calorie.submitData(listOf(sectionCalorie))

        val sectionProtein = DonutSection(
                name = "Alınan Protein",
                color = resources.getColor(R.color.green_500),
                amount = 100f
        )

        view.dv_daily_protein.cap = 1500f
        view.dv_daily_protein.submitData(listOf(sectionProtein))

        val sectionFat = DonutSection(
                name = "Alınan Yağ",
                color = resources.getColor(R.color.green_500),
                amount = 100f
        )

        view.dv_daily_fat.cap = 1500f
        view.dv_daily_fat.submitData(listOf(sectionFat))

        val sectionCarbohydrate = DonutSection(
                name = "Alınan KarbonHidrat",
                color = resources.getColor(R.color.green_500),
                amount = 100f
        )

        view.dv_daily_carbohydrate.cap = 1500f
        view.dv_daily_carbohydrate.submitData(listOf(sectionCarbohydrate))

        view.mv_drop_down.setOnClickListener {
            if(view.layout_protein_fat_carbohydrate.visibility == View.GONE){
                view.layout_protein_fat_carbohydrate.visibility = View.VISIBLE
                view.mv_drop_down.setImageDrawable(resources.getDrawable(R.drawable.icon_arrow_up))
            }
            else{
                view.layout_protein_fat_carbohydrate.visibility = View.GONE
                view.mv_drop_down.setImageDrawable(resources.getDrawable(R.drawable.icon_arrow_down))
            }
        }




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

    fun getFoods(): ArrayList<Food>{
        val foods = arrayListOf<Food>()
        foods.add(0,Food(1, "Tarhana", 105, 10,15,35, ServingType(1, "Kase")))
        foods.add(1,Food(2, "Kuru Fasulye", 155, 10,15,35, ServingType(1, "Tabak")))
        foods.add(2, Food(3, "Pilav", 200, 10,15,35, ServingType(1, "Tabak")))
        foods.add(2, Food(3, "Menemen", 180, 10,15,35, ServingType(1, "Tabak")))
        foods.add(2, Food(3, "Kabak Yemeği", 80, 10,15,35, ServingType(1, "Tabak")))

        return foods
    }


    fun getMeals(): ArrayList<Meal>{
        val meals = arrayListOf<Meal>()
        val foods = arrayListOf<Food>()

        foods.add(0,Food(1, "Tarhana", 105, 10,15,35, ServingType(1, "Kase")))
        foods.add(1,Food(2, "Kuru Fasulye", 155, 10,15,35, ServingType(1, "Tabak")))
        foods.add(2, Food(3, "Pilav", 200, 10,15,35, ServingType(1, "Tabak")))
        meals.add(Meal("123456", null, Timestamp.now(), 0, 0, 0, 0, MealType(1, "Kahvaltı")))
        meals.add(Meal("123456", foods, Timestamp.now(), 460, 30, 45, 105, MealType(2, "Öğle Yemeği")))
        meals.add(Meal("123456", foods, Timestamp.now(), 460, 30, 45, 105, MealType(3, "Akşam Yemeği")))

        return meals
    }
}