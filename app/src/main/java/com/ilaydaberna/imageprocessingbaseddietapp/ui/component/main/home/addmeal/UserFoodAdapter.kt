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

class UserFoodAdapter(private val context: Context,
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
        var amount = userFoodAmount[position]["amount"]?.toDouble()?.toInt().toString() + " "
        viewHolder.tvUserFoodName.text = userFoods.get(position).name
        viewHolder.tvUserFoodCalorie.text = userFoods.get(position).calorie.toString() + " kcal"
        viewHolder.tvUserFoodServingValue.text = amount + userFoods.get(position).servingType
        viewHolder.tvUserFoodProtein.text = "Protein:\n"+ userFoods.get(position).protein.toString()+ " gr"
        viewHolder.tvUserFoodFat.text = "Yağ:\n"+ userFoods.get(position).protein.toString()+ " gr"
        viewHolder.tvUserFoodCarbohydrate.text = "Karbonhidrat:\n"+ userFoods.get(position).carbohydrate.toString()+ " gr"

        viewHolder.layoutItemUserFood.setOnClickListener {
            //TODO
            // navigator.openAddMealFragment(dataSet.get()!!.get(position))
        }
    }

    override fun getItemCount() = userFoods.size

}


