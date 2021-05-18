package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.Food
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.Meal


class MealsAdapter(private val context: Context, private val dataSet: ArrayList<Meal>, private val foodList: List<Food>) : RecyclerView.Adapter<MealsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivMealType: ImageView
        val tvMealType: TextView
        val tvMealContent: TextView
        val tvMealTotalCalorie: TextView
        val layoutMeal: ConstraintLayout
        val layoutAddMeal: LinearLayout
        val rvFoodList: RecyclerView

        init {
            ivMealType = view.findViewById(R.id.iv_meal_type)
            tvMealType = view.findViewById(R.id.tv_meal_type)
            tvMealContent = view.findViewById(R.id.tv_meal_content)
            tvMealTotalCalorie = view.findViewById(R.id.tv_meal_total_calorie)
            layoutMeal = view.findViewById(R.id.layout_meal)
            layoutAddMeal = view.findViewById(R.id.layout_add_meal)
            rvFoodList = view.findViewById(R.id.rv_food_list)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.cardview_add_meal, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvMealType.text = dataSet.get(position).type.description
        when(dataSet.get(position).type.id){
            1 -> viewHolder.ivMealType.setImageDrawable(context.getDrawable(R.drawable.breakfast))
            2 -> viewHolder.ivMealType.setImageDrawable(context.getDrawable(R.drawable.launch))
            3 -> viewHolder.ivMealType.setImageDrawable(context.getDrawable(R.drawable.dinner))
            4 -> viewHolder.ivMealType.setImageDrawable(context.getDrawable(R.drawable.snacks))
            else -> {
                viewHolder.ivMealType.setImageDrawable(context.getDrawable(R.drawable.icon_man))
            }
        }


        if(dataSet.get(position).contents != null){
            var i:Int = 1
            var strContent: String = dataSet.get(position).contents!![0].name
            while(i < dataSet.get(position).contents!!.size){
                strContent = strContent + ", "+ dataSet.get(position).contents!![i].name
                i = i + 1
            }
            viewHolder.tvMealContent.text = strContent
            viewHolder.tvMealTotalCalorie.text = dataSet.get(position).totalCalorie.toString() + " cal"
        }
        else{
            viewHolder.tvMealTotalCalorie.text = "0 cal"
            viewHolder.tvMealContent.text = " - "
        }

        viewHolder.layoutMeal.setOnClickListener {
            if(viewHolder.layoutAddMeal.visibility == View.VISIBLE){
                viewHolder.layoutAddMeal.visibility = View.GONE
            }
            else{
                viewHolder.layoutAddMeal.visibility = View.VISIBLE
            }
        }



        viewHolder.rvFoodList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewHolder.rvFoodList.adapter = FoodsAdapter(foodList)
        viewHolder.rvFoodList.addOnItemTouchListener(object : OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val action = e.action
                when (action) {
                    MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                TODO("Not yet implemented")
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun getItemCount() = dataSet.size

}


