package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home.addmeal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.ItemFoodListBinding
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.Food

class FoodsAdapter : ListAdapter<Food, FoodsAdapter.FoodHolder>(DIFF_CALLBACK){
    var itemClickListener: (Food) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val binding = ItemFoodListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )

        return FoodHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) =
            holder.bind(getItem(position))

    class FoodHolder(
            private val binding: ItemFoodListBinding,
            private val itemClickListener: (Food) -> Unit)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {

            binding.tvFoodName.text = food.name
            binding.tvFoodCalorie.text = food.calorie.toString() + " kcal"

            binding.root.setOnClickListener {
                itemClickListener(food)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Food>() {
            override fun areItemsTheSame(oldItem: Food, newItem: Food) =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Food, newItem: Food) =
                    oldItem == newItem
        }
    }
}
