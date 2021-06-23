package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home.addmeal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.Food
import java.text.DecimalFormat

class UserFoodAdapter(private val navigator: AddMealNavigator,
                      private val userFoods: ArrayList<Food>,
                      private val userFoodAmount : ArrayList<Map<String,String>>
) : RecyclerView.Adapter<UserFoodAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserFoodName: TextView
        val tvUserFoodCalorie: TextView
        val tvUserFoodServingValue: TextView
        val tvUserFoodProtein: TextView
        val tvUserFoodFat: TextView
        val tvUserFoodCarbohydrate: TextView
        val layoutItemUserFood: LinearLayout

        init {
            tvUserFoodName = view.findViewById(R.id.tv_user_food_name)
            tvUserFoodCalorie = view.findViewById(R.id.tv_user_food_calorie)
            tvUserFoodServingValue = view.findViewById(R.id.tv_user_food_serving_value)
            tvUserFoodProtein = view.findViewById(R.id.tv_user_food_protein)
            tvUserFoodFat = view.findViewById(R.id.tv_user_food_fat)
            tvUserFoodCarbohydrate = view.findViewById(R.id.tv_user_food_carbohydrate)
            layoutItemUserFood = view.findViewById(R.id.layout_item_user_food)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_user_food_list, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var amount = userFoodAmount[position]["amount"]?.toDouble()?.toString() + " "
        var foodCalorie = userFoodAmount[position]["amount"]?.toDouble()?.times(userFoods.get(position).calorie)
        var foodProtein = userFoodAmount[position]["amount"]?.toDouble()?.times(userFoods.get(position).protein)
        var foodCarbohydrate = userFoodAmount[position]["amount"]?.toDouble()?.times(userFoods.get(position).carbohydrate)
        var foodFat = userFoodAmount[position]["amount"]?.toDouble()?.times(userFoods.get(position).fat)

        viewHolder.tvUserFoodName.text = userFoods.get(position).name
        viewHolder.tvUserFoodServingValue.text = amount + userFoods.get(position).servingType
        viewHolder.tvUserFoodCalorie.text = DecimalFormat("##.##").format(foodCalorie).toString() + " kcal"
        viewHolder.tvUserFoodCarbohydrate.text = "Karbonhidrat:\n"+ DecimalFormat("##.##").format(foodCarbohydrate).toString()+ " gr"
        viewHolder.tvUserFoodProtein.text = "Protein:\n"+ DecimalFormat("##.##").format(foodProtein).toString()+ " gr"
        viewHolder.tvUserFoodFat.text = "Yağ:\n"+ DecimalFormat("##.##").format(foodFat).toString()+ " gr"

        viewHolder.layoutItemUserFood.setOnClickListener {
            navigator.removeUserFood(userFood = userFoods.get(position), userFoodAmount[position]["amount"]?.toDouble()?:1.0)
        }
    }

    override fun getItemCount() = userFoods.size

}


