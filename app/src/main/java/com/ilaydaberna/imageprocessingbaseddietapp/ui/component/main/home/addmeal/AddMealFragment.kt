package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home.addmeal

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.*
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.MainActivity
import com.ilaydaberna.imageprocessingbaseddietapp.util.isAmountValid
import kotlinx.android.synthetic.main.dialog_add_food_amount.view.*
import kotlinx.android.synthetic.main.fragment_add_meal.view.*
import java.text.DecimalFormat

class AddMealFragment : Fragment() {

    private var adapterFood = FoodsAdapter()
    private lateinit var adapterUserFood: UserFoodAdapter

    private var userFoodList = ArrayList<Food>()
    private var userFoodAmount = ArrayList<Map<String, String>>()

    private lateinit var userMealItem: MealItem

    private val currentUser: FirebaseUser? = FirebaseSource().getAuth().currentUser

    private var totalCalorie = 0.0
    private var totalProtein = 0.0
    private var totalFat = 0.0
    private var totalCarbohydrate: Double = 0.0

    lateinit var mView: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_add_meal, container, false)
        mView = view

        view.rv_food_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.rv_user_food.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.rv_food_list.adapter = adapterFood

        if(arguments != null){
            userMealItem = (requireArguments().get("meal") as MealItem)

            when(userMealItem.type){
                "Breakfast" -> view.tv_meal_title.text = "Kahvaltım"
                "Lunch" -> view.tv_meal_title.text = "Öğle Yemeğim"
                "Dinner" -> view.tv_meal_title.text = "Akşam Yemeğim"
                "Snacks" -> view.tv_meal_title.text = "Ara Öğünüm"
            }

            totalCalorie = userMealItem.totalCalorie.toDouble()
            totalCarbohydrate = userMealItem.totalCarbohydrate.toDouble()
            totalProtein = userMealItem.totalProtein.toDouble()
            totalFat = userMealItem.totalFat.toDouble()

            userFoodList = userMealItem.contents as ArrayList<Food>
            userFoodAmount = (userMealItem.amounts?: arrayListOf()) as ArrayList<Map<String, String>>

            adapterUserFood = UserFoodAdapter(context = requireContext(), userFoods = userFoodList, userFoodAmount = userFoodAmount)
            view.rv_user_food.adapter = adapterUserFood
            adapterUserFood.notifyDataSetChanged()
            setTotalView(view, userMealItem)
        }

        getFoods()

        adapterFood.itemClickListener = {
            openAmountDialog(it)
        }

        view.sw_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("SearchViewTextChanged", newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("onQueryTextSubmit", query)
                adapterFood.submitList(filteredFoods(query = query))
                return false
            }

        })

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
        mDialogView.tv_serving_type.text = food.servingType

        mDialogView.btn_dialog_amount_save.setOnClickListener {
            if(mDialogView.et_amount.text.isEmpty()){
                mDialogView.et_amount.setError("Bu alan boş bırakılamaz")
            }
            else if(mDialogView.et_amount.text.toString().isAmountValid()){
                amount = mDialogView.et_amount.text.toString()
                addUserFood(amount.toDouble(), food)
                mAlertDialog.dismiss()
            }
            else{
                mDialogView.et_amount.setError("Geçerli bir değer giriniz")
            }
        }
    }

    private fun addUserFood(amount: Double, food: Food){
        (activity as MainActivity?)?.showLoading()
        userFoodList.add(food)
        userFoodAmount.add(mapOf("foodID" to food.id.toString(), "amount" to amount.toString()))
        totalCalorie += food.calorie * amount
        totalFat += food.fat * amount
        totalProtein += food.protein * amount
        totalCarbohydrate += food.carbohydrate * amount

        val thisMeal = UserMeals.Meal(
                totalCalorie = totalCalorie.toInt(),
                totalCarbohydrate = totalCarbohydrate.toInt(),
                totalFat = totalFat.toInt(),
                totalProtein = totalProtein.toInt(),
                contents = userFoodAmount
        )

        FirestoreSource.updateUserMealForToday(currentUser = currentUser,
                userMeal = thisMeal,
                mealType = userMealItem.type,
                successHandler = {
                    updateTotalView()
                    (activity as MainActivity?)?.hideLoading()
                },
                failHandler = {
                    (activity as MainActivity?)?.hideLoading()
                }
        )
    }

    private fun setTotalView(view: View, meal: MealItem){
        view.tv_user_meal_total_calorie.text = DecimalFormat("##.##").format(meal.totalCalorie) +" kcal"
        view.tv_user_meal_total_protein.text = DecimalFormat("##.##").format(meal.totalProtein) +" gr"
        view.tv_user_meal_total_carbohydrate.text = DecimalFormat("##.##").format(meal.totalCarbohydrate) +" gr"
        view.tv_user_meal_total_fat.text = DecimalFormat("##.##").format(meal.totalFat) +" gr"
    }

    private fun updateTotalView(){
        view?.tv_user_meal_total_calorie?.text = DecimalFormat("##.##").format(totalCalorie) +" kcal"
        view?.tv_user_meal_total_protein?.text = DecimalFormat("##.##").format(totalProtein) +" gr"
        view?.tv_user_meal_total_carbohydrate?.text = DecimalFormat("##.##").format(totalCarbohydrate) +" gr"
        view?.tv_user_meal_total_fat?.text = DecimalFormat("##.##").format(totalFat) +" gr"
    }


    private fun filteredFoods(query: String): MutableList<Food>{
        val lowerCaseQuery = query.toLowerCase()
        val foods = FoodSingleton.food.get()
        val filteredFoods: MutableList<Food> = ArrayList()
        if (foods != null) {
            for (food: Food in foods) {
                val foodName: String = food.name.toLowerCase()
                if (foodName.contains(lowerCaseQuery)) {
                    filteredFoods.add(food)
                }
            }
        }
        return filteredFoods
    }

    private fun getFoods(){
        adapterFood.submitList(FoodSingleton.food.get())
    }
}