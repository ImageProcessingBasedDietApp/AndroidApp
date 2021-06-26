package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.futured.donut.DonutProgressView
import app.futured.donut.DonutSection
import com.google.firebase.auth.FirebaseUser
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.*
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.MainActivity
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.stream.Collectors

class HomeFragment : Fragment(), HomeNavigator{

    private lateinit var adapter: MealsAdapter
    private val currentUser: FirebaseUser? = FirebaseSource().getAuth().currentUser
    private var mealItems: ObservableField<ArrayList<MealItem>> = ObservableField(arrayListOf())
    private lateinit var dailyCalorie: DonutProgressView
    private lateinit var dailyProtein: DonutProgressView
    private lateinit var dailyCarbohydrate: DonutProgressView
    private lateinit var dailyFat: DonutProgressView

    var totalCalorie: Int = 0
    var totalCarbohydrate: Int = 0
    var totalProtein: Int = 0
    var totalFat: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.rv_meal_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        dailyCalorie = view.dv_daily_calorie
        dailyProtein = view.dv_daily_protein
        dailyCarbohydrate = view.dv_daily_fat
        dailyFat = view.dv_daily_carbohydrate
        setDonutProgressCapValues()

        view.mv_drop_down.setOnClickListener {
            if(view.layout_protein_fat_carbohydrate.visibility == View.GONE) {
                view.layout_protein_fat_carbohydrate.visibility = View.VISIBLE
                view.mv_drop_down.setImageDrawable(resources.getDrawable(R.drawable.icon_arrow_up))
            }
            else {
                view.layout_protein_fat_carbohydrate.visibility = View.GONE
                view.mv_drop_down.setImageDrawable(resources.getDrawable(R.drawable.icon_arrow_down))
            }
        }

        //Meals
        adapter = MealsAdapter(requireContext(), mealItems, this)
        view.rv_meal_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.rv_meal_list.adapter = adapter
        getUserMeals()

        return view
    }

    private fun getUserFoods(contents: ArrayList<Map<String, String>>): List<Food?>? {
        return FoodSingleton.food.get()?.stream()
                ?.filter { food ->
                    contents.stream()
                            .anyMatch { content ->
                                try {
                                    content["foodID"]!!.toString() == food.id.toString()
                                } catch (e: Exception) {
                                    Log.i("", "")
                                    false
                                }
                            }
                }
                ?.collect(Collectors.toList())
    }

    private fun getMealItems(meals: UserMeals){
        var tempMealItems: ArrayList<MealItem> = arrayListOf()
        if(meals.breakfast != null){
            tempMealItems.add(
                MealItem(
                    meals.breakfast?.totalCalorie?:0,
                    meals.breakfast?.totalCarbohydrate?:0,
                    meals.breakfast?.totalFat?:0,
                    meals.breakfast?.totalProtein?:0,
                    "Breakfast",
                    meals.breakfast?.contents?.let { getUserFoods(it) },
                    meals.breakfast?.contents
                )
            )
        }

        if(meals.lunch != null){
            tempMealItems.add(
                    MealItem(
                            meals.lunch?.totalCalorie?:0,
                            meals.lunch?.totalCarbohydrate?:0,
                            meals.lunch?.totalFat?:0,
                            meals.lunch?.totalProtein?:0,
                            "Lunch",
                            meals.lunch?.contents?.let { getUserFoods(it) },
                            meals.lunch?.contents
                    )
            )
        }

        if(meals.dinner != null){
            tempMealItems.add(
                    MealItem(
                            meals.dinner?.totalCalorie?:0,
                            meals.dinner?.totalCarbohydrate?:0,
                            meals.dinner?.totalFat?:0,
                            meals.dinner?.totalProtein?:0,
                            "Dinner",
                            meals.dinner?.contents?.let { getUserFoods(it) },
                            meals.dinner?.contents
                    )
            )
        }

        if(meals.snacks != null){
            tempMealItems.add(
                    MealItem(
                            meals.snacks?.totalCalorie?:0,
                            meals.snacks?.totalCarbohydrate?:0,
                            meals.snacks?.totalFat?:0,
                            meals.snacks?.totalProtein?:0,
                            "Snacks",
                            meals.snacks?.contents?.let { getUserFoods(it) },
                            meals.snacks?.contents
                    )
            )
        }

        mealItems.set(tempMealItems)
        adapter.notifyDataSetChanged()
    }

    private fun getUserMeals(){
        (activity as MainActivity?)?.showLoading()
        FirestoreSource.getUserMealsForToday(currentUser = currentUser,
            successHandler = {
                getMealItems(it)
                setDonutProgressViews(it)
                (activity as MainActivity?)?.hideLoading()
            },
            failHandler = {
                (activity as MainActivity?)?.hideLoading()
            }
        )
    }

    private fun setDonutProgressCapValues() {
        dailyCalorie.cap = UserInfo.user.get()?.dailyCalorie?.toFloat() ?: 1500F
        dailyProtein.cap = UserInfo.user.get()?.dailyProtein?.toFloat() ?: 450F
        dailyFat.cap = UserInfo.user.get()?.dailyFat?.toFloat() ?: 330F
        dailyCarbohydrate.cap = UserInfo.user.get()?.dailyCarbohydrate?.toFloat() ?: 720F
    }

    private fun setDonutProgressViews(meals: UserMeals) {
        val totalCalorie = (meals.breakfast?.totalCalorie ?: 0) + (meals.lunch?.totalCalorie
            ?: 0) + (meals.dinner?.totalCalorie ?: 0) + (meals.snacks?.totalCalorie ?: 0)
        val totalFat = (meals.breakfast?.totalFat ?: 0) + (meals.lunch?.totalFat
            ?: 0) + (meals.dinner?.totalFat ?: 0) + (meals.snacks?.totalFat ?: 0)
        val totalProtein = (meals.breakfast?.totalProtein ?: 0) + (meals.lunch?.totalProtein
            ?: 0) + (meals.dinner?.totalProtein ?: 0) + (meals.snacks?.totalProtein ?: 0)
        val totalCarbohydrate =
            (meals.breakfast?.totalCarbohydrate ?: 0) + (meals.lunch?.totalCarbohydrate
                ?: 0) + (meals.dinner?.totalCarbohydrate ?: 0) + (meals.snacks?.totalCarbohydrate
                ?: 0)

        view?.tv_daily_calorie_value?.text =
            totalCalorie.toString() + " kcal / \n" + UserInfo.user.get()?.dailyCalorie.toString() + " kcal"
        view?.tv_daily_carbohydrate_value?.text =
            totalCarbohydrate.toString() + " gr /\n " + UserInfo.user.get()?.dailyCarbohydrate.toString() + " gr"
        view?.tv_daily_protein_value?.text =
            totalProtein.toString() + " gr /\n " + UserInfo.user.get()?.dailyProtein.toString() + " gr"
        view?.tv_daily_fat_value?.text =
            totalFat.toString() + " gr /\n " + UserInfo.user.get()?.dailyFat.toString() + " gr"

        //Donut Graph
        val sectionCalorie = DonutSection(
            name = "Alınan Kalori",
            color = resources.getColor(R.color.green_500),
            amount = totalCalorie.toFloat()
        )

        dailyCalorie.submitData(listOf(sectionCalorie))

        val sectionProtein = DonutSection(
                name = "Alınan Protein",
                color = resources.getColor(R.color.green_500),
                amount = totalProtein.toFloat()
        )

        dailyProtein.submitData(listOf(sectionProtein))

        val sectionFat = DonutSection(
                name = "Alınan Yağ",
                color = resources.getColor(R.color.green_500),
                amount = totalFat.toFloat()
        )

        dailyFat.submitData(listOf(sectionFat))

        val sectionCarbohydrate = DonutSection(
                name = "Alınan KarbonHidrat",
                color = resources.getColor(R.color.green_500),
                amount = totalCarbohydrate.toFloat()
        )

        dailyCarbohydrate.submitData(listOf(sectionCarbohydrate))
    }

    override fun openAddMealFragment(mealItem: MealItem) {
        val bundle = bundleOf("meal" to mealItem)
        findNavController().navigate(R.id.actionToAddMealFragment, bundle)
    }
}