package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home.addmeal

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.*
import com.ilaydaberna.imageprocessingbaseddietapp.util.isAmountValid
import com.ilaydaberna.imageprocessingbaseddietapp.util.isNameSurnameValid
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.dialog_add_food_amount.view.*
import kotlinx.android.synthetic.main.dialog_enter_int_value.view.*
import kotlinx.android.synthetic.main.dialog_enter_int_value.view.btn_dialog_save
import kotlinx.android.synthetic.main.dialog_enter_string_value.view.*
import kotlinx.android.synthetic.main.dialog_enter_string_value.view.btn_dialog_name_surname_save
import kotlinx.android.synthetic.main.dialog_enter_weight.view.*
import kotlinx.android.synthetic.main.fragment_add_meal.view.*

class AddMealFragment : Fragment() {

    private var adapterFood = FoodsAdapter()
    private var adapterUserFood = UserFoodAdapter()

    private var userFoodList = listOf<Food>()
    private var userFoodAmount = listOf<Map<String,String>?>()

    private lateinit var userMealItem: MealItem

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

        if(arguments != null){
            userMealItem = (requireArguments().get("meal") as MealItem)
            view.tv_meal_title.text = userMealItem.type
            userFoodList = userMealItem.contents as List<Food>
            userFoodAmount = userMealItem.amounts?: listOf()
            setTotalView(view, userMealItem)
        }

        getFoods()

        adapterFood.itemClickListener = {
            openAmountDialog(it)
            //addUserFood(it)
            //updateTotalView(view)
        }

        adapterUserFood.itemClickListener = {
            //removeUserFood(it)
            //updateTotalView(view)
        }

        view.btn_add_meal_back.setOnClickListener {
            activity?.onBackPressed()
        }

        return view
    }

    private fun openAmountDialog(food: Food){
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_food_amount, null)
        val mBuilder = AlertDialog.Builder(context).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        var amount: String = ""

        mDialogView.btn_dialog_amount_save.setOnClickListener {
            if(mDialogView.et_amount.text.isEmpty()){
                mDialogView.et_amount.setError("Bu alan boş bırakılamaz")
            }
            else if(mDialogView.et_amount.text.toString().isAmountValid()){
                amount = mDialogView.et_amount.text.toString()
                mAlertDialog.dismiss()
            }
            else{
                mDialogView.et_amount.setError("Geçerli bir değer giriniz")
            }
        }
    }

    private fun setTotalView(view: View, meal: MealItem){
        view.tv_user_meal_total_calorie.text = meal.totalCalorie.toString() +" kcal"
        view.tv_user_meal_total_protein.text = meal.totalProtein.toString() +" gr"
        view.tv_user_meal_total_carbohydrate.text = meal.totalCarbohydrate.toString() +" gr"
        view.tv_user_meal_total_fat.text = meal.totalFat.toString() +" gr"
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
        adapterFood.submitList(FoodSingleton.food.get())
    }
}