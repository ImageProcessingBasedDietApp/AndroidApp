package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.futured.donut.DonutSection
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.*
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.MainActivity
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.channels.consumesAll

class HomeFragment : Fragment() {

    private val adapter = MealsAdapter()
    val currentUser: FirebaseUser? = FirebaseSource().getAuth().currentUser
    var mealItems = arrayListOf<MealItem>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.rv_meal_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //Donut Graph
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
        view.rv_meal_list.adapter = adapter
        getUserMeals()

        adapter.itemClickListener = {
            findNavController().navigate(R.id.actionToAddMealFragment)
        }

        return view
    }

    private fun getUserFoods(contents: ArrayList<Map<String, Int>>): ArrayList<Food?> {
        var mealContentFoods = arrayListOf<Food?>()
        val i = 0
        for (content in contents) {
            FirestoreSource.getFoodById(foodID = content.get("foodID").toString(),
                successHandler = {
                    mealContentFoods.add(it)
                },
                failHandler = {

                }
            )
        }
        return mealContentFoods
    }

    private fun getMealItems(meals: UserMeals){
        if(meals.breakfast != null){
            mealItems.add(
                MealItem(
                    meals.breakfast?.totalCalorie?:0,
                    meals.breakfast?.totalCarbohydrate?:0,
                    meals.breakfast?.totalFat?:0,
                    meals.breakfast?.totalProtein?:0,
                    "Kahvaltı",
                    meals.breakfast?.contents?.let { getUserFoods(it) }
                )
            )
        }
    }

    private fun getUserMeals(){
        (activity as MainActivity?)?.showLoading()
        FirestoreSource.getUserMealsForToday(currentUser = currentUser,
            successHandler = {
                (activity as MainActivity?)?.hideLoading()
                if(it != null){
                    getMealItems(it)
                }
                adapter.submitList(mealItems)
            },
            failHandler = {
                (activity as MainActivity?)?.hideLoading()
            }
        )
    }
}