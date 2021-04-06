package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.Food

class FoodsAdapter(private val foodList: List<Food>): RecyclerView.Adapter<FoodsAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivFoodState: ImageView
        val tvFoodName: TextView
        val tvFoodAmount: TextView
        init {
            ivFoodState = view.findViewById(R.id.iv_food_state)
            tvFoodName = view.findViewById(R.id.tv_food_name)
            tvFoodAmount = view.findViewById(R.id.tv_food_amount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_list, parent, false)

        return FoodsAdapter.ViewHolder(view)
    }



    override fun onBindViewHolder(holder: FoodsAdapter.ViewHolder, position: Int) {
        holder.tvFoodName.text = foodList.get(position).name
        holder.tvFoodAmount.text = foodList.get(position).servingType.description
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

}