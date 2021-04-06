package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.Meal


class MealsAdapter(private val dataSet: ArrayList<Meal>) : RecyclerView.Adapter<MealsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivMealType: ImageView
        val tvMealType: TextView
        val tvMealContent: TextView
        val tvMealTotalCalorie: TextView

        init {
            ivMealType = view.findViewById(R.id.iv_meal_type)
            tvMealType = view.findViewById(R.id.tv_meal_type)
            tvMealContent = view.findViewById(R.id.tv_meal_content)
            tvMealTotalCalorie = view.findViewById(R.id.tv_meal_total_calorie)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.cardview_add_meal, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvMealType.text = dataSet.get(position).type.description
        if(dataSet.get(position).contents.size > 0){
            viewHolder.tvMealTotalCalorie.text = dataSet.get(position).totalCalorie.toString() + " cal"
        }
        else{
            viewHolder.tvMealTotalCalorie.text = "0 cal"
            viewHolder.tvMealContent.text = " - "
        }
    }

    override fun getItemCount() = dataSet.size

}
