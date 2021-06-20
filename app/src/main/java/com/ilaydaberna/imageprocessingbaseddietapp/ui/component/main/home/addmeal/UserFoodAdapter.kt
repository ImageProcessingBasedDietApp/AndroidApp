package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home.addmeal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.ItemUserFoodListBinding
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.Food

class UserFoodAdapter : ListAdapter<Food, UserFoodAdapter.UserFoodHolder>(DIFF_CALLBACK){
    var itemClickListener: (Food) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFoodHolder {
        val binding = ItemUserFoodListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return UserFoodHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: UserFoodHolder, position: Int) =
        holder.bind(getItem(position))

    class UserFoodHolder(
        private val binding: ItemUserFoodListBinding,
        private val itemClickListener: (Food) -> Unit)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {

            binding.tvUserFoodName.text = food.name
            binding.tvUserFoodCalorie.text = food.calorie.toString() + " kcal"
            binding.tvUserFoodServingValue.text = "1 " +food.servingType
            binding.tvUserFoodProtein.text = "Protein:\n"+food.protein.toString()+ " gr"
            binding.tvUserFoodFat.text = "Yağ:\n"+food.protein.toString()+ " gr"
            binding.tvUserFoodCarbohydrate.text = "Karbonhidrat:\n"+food.carbohydrate.toString()+ " gr"

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