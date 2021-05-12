package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.follow

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirebaseSource
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirestoreSource
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.User
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.UserInfo
import kotlinx.android.synthetic.main.dialog_enter_weight.view.*
import kotlinx.android.synthetic.main.fragment_follow.view.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

class FollowFragment : Fragment() {

    lateinit var user: User
    var cup_of_tea: Int = 0 //Firebaseden setlenecek.
    var cup_of_coffee: Int = 0 //Firebaseden setlenecek.
    var weight: Double = 0.0 //Kullanıcıdan alınan kiloya setlenecek.
    lateinit var weightText: String
    var newWeight: String? = ""
    val currentUser: FirebaseUser? = FirebaseSource().getAuth().currentUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_follow, container, false)

        Thread(Runnable {
            user = UserInfo.user.get()!!
        }).start()

        Thread.sleep(1000)
        weight = user.weight.toDouble()
        weightText = weight.toString()

        view.tv_weight.text = weight.toString()

        view.btn_add_water.setOnClickListener() {
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_select_water_amount, null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
        }

        //Handle the Weight Which is Entered
        view.iv_minus.setOnClickListener() {
            clickedToDecreaseWeight()
        }

        view.iv_plus.setOnClickListener() {
            clickedToIncreaseWeight()
        }

        view.tv_weight.setOnClickListener() {
            clickedWeightText()
        }

        view.tea_minus.setOnClickListener() {
            clickedTeaMinus()
        }

        view.tea_plus.setOnClickListener() {
            clickedTeaPlus()
        }

        view.coffee_minus.setOnClickListener() {
            clickedCoffeeMinus()
        }

        view.coffee_plus.setOnClickListener() {
            clickedCoffeePlus()
        }


        //Handle Fragment Switching Water - Tea - Coffee
        view.iv_coffee_on_water.setOnClickListener() {
            view.layout_water.visibility = View.GONE
            view.layout_coffee.visibility = View.VISIBLE
        }

        view.iv_tea_on_water.setOnClickListener() {
            view.layout_water.visibility = View.GONE
            view.layout_tea.visibility = View.VISIBLE
        }

        view.iv_tea_on_coffee.setOnClickListener() {
            view.layout_coffee.visibility = View.GONE
            view.layout_tea.visibility = View.VISIBLE
        }

        view.iv_water_on_coffee.setOnClickListener() {
            view.layout_coffee.visibility = View.GONE
            view.layout_water.visibility = View.VISIBLE
        }

        view.iv_water_on_tea.setOnClickListener() {
            view.layout_tea.visibility = View.GONE
            view.layout_water.visibility = View.VISIBLE
        }

        view.iv_coffee_on_tea.setOnClickListener() {
            view.layout_tea.visibility = View.GONE
            view.layout_coffee.visibility = View.VISIBLE
        }

        view.tea_information.setOnClickListener() {
            showInformationDialog()
        }

        view.coffee_information.setOnClickListener() {
            showInformationDialog()
        }

        for(i in 1..12) {
            val iv = ImageView(context)
            //iv.layoutParams = LinearLayout.LayoutParams(150, 150)
            iv.setImageResource(R.drawable.tea_empty)

            val param =  GridLayout.LayoutParams()
            param.height = 150
            param.width = 150
            param.setGravity(Gravity.CENTER)

            view?.gl_tea?.addView(iv, param)

        }

        for(i in 1..12) {
            val iv = ImageView(context)
            //iv.layoutParams = LinearLayout.LayoutParams(150, 150)
            iv.setImageResource(R.drawable.icon_turkish_coffee_empty)

            val param =  GridLayout.LayoutParams()
            param.height = 150
            param.width = 150
            param.setGravity(Gravity.CENTER)

            view?.gl_coffee?.addView(iv, param)

        }

        return view
    }

    override fun onStop() {
        super.onStop()

        val calendar = Calendar.getInstance()
        val date: Date = calendar.getTime()
        val timestamp: Long = date.getTime()
        var weightDouble = weightText.toDouble()
        UserInfo.user.get()?.weight = weightDouble
        if (currentUser != null) {
            FirestoreSource().saveWeight(currentUser, weightDouble, timestamp)
            UserInfo.user.get()?.let { FirestoreSource().saveUser(currentUser, it) }
        }

    }

    fun clickedTeaMinus() {
        if (cup_of_tea > 0) {
            cup_of_tea -= 1
        } else {
            cup_of_tea = 0
        }

        val v = view?.gl_tea?.getChildAt(cup_of_tea) as ImageView
        v.setImageResource(R.drawable.tea_empty)
        view?.amount_of_tea?.text = "Günlük içilen çay miktarı = " + cup_of_tea.toString() + " Bardak"
    }

    fun clickedCoffeeMinus() {
        if (cup_of_coffee > 0) {
            cup_of_coffee -= 1
        } else {
            cup_of_coffee = 0
        }

        val v = view?.gl_coffee?.getChildAt(cup_of_coffee) as ImageView
        v.setImageResource(R.drawable.icon_turkish_coffee_empty)
        view?.amount_of_coffee?.text = "Günlük içilen çay miktarı = " + cup_of_coffee.toString() + " Bardak"
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun clickedTeaPlus() {
        if (cup_of_tea < 12) {
            cup_of_tea += 1
        } else {
            cup_of_tea = 12
        }

        val v = view?.gl_tea?.getChildAt(cup_of_tea-1) as ImageView
        v.setImageResource(R.drawable.tea_full)
        view?.amount_of_tea?.text = "Günlük içilen çay miktarı = " + cup_of_tea.toString() + " Bardak"
    }

    fun clickedCoffeePlus() {
        if (cup_of_coffee < 12) {
            cup_of_coffee += 1
        } else {
            cup_of_coffee = 12
        }

        val v = view?.gl_coffee?.getChildAt(cup_of_coffee-1) as ImageView
        v.setImageResource(R.drawable.icon_turkish_coffee)
        view?.amount_of_coffee?.text = "Günlük içilen çay miktarı = " + cup_of_coffee.toString() + " Bardak"
    }

    fun clickedWeightText() {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_enter_weight, null)
        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mDialogView.np_dialog_weight_int.maxValue = 597
        mDialogView.np_dialog_weight_int.minValue = 0
        mDialogView.np_dialog_weight_int.value = weight.toInt()
        var decimal = weight - weight.toInt()
        decimal *= 10

        mDialogView.np_dialog_weight_decimal.maxValue = 9
        mDialogView.np_dialog_weight_decimal.minValue = 0

        mDialogView.np_dialog_weight_decimal.value = decimal.toInt()
        mDialogView.btn_dialog_save.setOnClickListener {
            mAlertDialog.dismiss()
            newWeight = mDialogView.np_dialog_weight_int.value.toString() + "." + mDialogView.np_dialog_weight_decimal.value.toString()
            view?.tv_weight?.text = newWeight
            weight = newWeight.toString().toDouble()
            weightText = newWeight.toString()
        }

    }

    fun clickedToIncreaseWeight() {
        weight += 0.1
        var text: String = ""
        text = if (weight in 0.0..9.9) {
            DecimalFormat("0.0").format(weight)
        } else if (weight in 9.99..99.9) {
            DecimalFormat("00.0").format(weight)
        } else {
            DecimalFormat("000.0").format(weight)
        }
        weightText = text
        view?.tv_weight?.text = text
    }

    fun clickedToDecreaseWeight() {
        if (weight >= 0.1) {
            weight -= 0.1
        } else {
            weight = 0.0
        }

        var text: String = ""
        text = if (weight in 0.0..9.9) {
            DecimalFormat("0.0").format(weight)
        } else if (weight in 9.9..99.9) {
            DecimalFormat("00.0").format(weight)
        } else {
            DecimalFormat("000.0").format(weight)
        }
        weightText = text
        view?.tv_weight?.text = text
    }

    fun showInformationDialog() {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_beverage_information, null)
        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
    }
}