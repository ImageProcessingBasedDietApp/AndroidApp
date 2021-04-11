package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.follow

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ilaydaberna.imageprocessingbaseddietapp.R
import kotlinx.android.synthetic.main.dialog_enter_weight.view.*
import kotlinx.android.synthetic.main.fragment_follow.view.*
import java.text.DecimalFormat

class FollowFragment : Fragment() {

    var cup_of_tea: Int = 0 //Firebaseden setlenecek.
    var weight: Double = 49.0 //Kullanıcıdan alınan kiloya setlenecek.
    var newWeight: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_follow, container, false)



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
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_beverage_information, null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
        }
        return view
    }

    fun clickedTeaMinus() {
        if (cup_of_tea > 0) {
            cup_of_tea -= 1
        } else {
            cup_of_tea = 0
        }

        when (cup_of_tea) {
            0 -> view?.tea_1?.setImageResource(R.drawable.tea_empty)
            1 -> view?.tea_2?.setImageResource(R.drawable.tea_empty)
            2 -> view?.tea_3?.setImageResource(R.drawable.tea_empty)
            3 -> view?.tea_4?.setImageResource(R.drawable.tea_empty)
            4 -> view?.tea_5?.setImageResource(R.drawable.tea_empty)
            5 -> view?.tea_6?.setImageResource(R.drawable.tea_empty)
            6 -> view?.tea_7?.setImageResource(R.drawable.tea_empty)
            7 -> view?.tea_8?.setImageResource(R.drawable.tea_empty)
            8 -> view?.tea_9?.setImageResource(R.drawable.tea_empty)
            9 -> view?.tea_10?.setImageResource(R.drawable.tea_empty)
            10 -> view?.tea_11?.setImageResource(R.drawable.tea_empty)
            11 -> view?.tea_12?.setImageResource(R.drawable.tea_empty)
        }
        view?.amount_of_tea?.text = "Günlük içilen çay miktarı = " + cup_of_tea.toString() + " Bardak"
    }

    fun clickedTeaPlus() {
        if (cup_of_tea < 12) {
            cup_of_tea += 1
        } else {
            cup_of_tea = 12
        }

        when (cup_of_tea) {
            1 -> view?.tea_1?.setImageResource(R.drawable.tea_full)
            2 -> view?.tea_2?.setImageResource(R.drawable.tea_full)
            3 -> view?.tea_3?.setImageResource(R.drawable.tea_full)
            4 -> view?.tea_4?.setImageResource(R.drawable.tea_full)
            5 -> view?.tea_5?.setImageResource(R.drawable.tea_full)
            6 -> view?.tea_6?.setImageResource(R.drawable.tea_full)
            7 -> view?.tea_7?.setImageResource(R.drawable.tea_full)
            8 -> view?.tea_8?.setImageResource(R.drawable.tea_full)
            9 -> view?.tea_9?.setImageResource(R.drawable.tea_full)
            10 -> view?.tea_10?.setImageResource(R.drawable.tea_full)
            11 -> view?.tea_11?.setImageResource(R.drawable.tea_full)
            12 -> view?.tea_12?.setImageResource(R.drawable.tea_full)
        }

        view?.amount_of_tea?.text = "Günlük içilen çay miktarı = " + cup_of_tea.toString() + " Bardak"
    }

    fun clickedWeightText() {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_enter_weight, null)
        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.btn_dialog_save.setOnClickListener {
            mAlertDialog.dismiss()
            newWeight = mDialogView.et_dialog_weight.text.toString()

            if (!newWeight.equals("") && !newWeight?.contains(".")!!) {
                newWeight = "$newWeight.0"
            }

            if (newWeight.equals("")) {
                newWeight = "0.0"
            }

            view?.tv_weight?.text = newWeight
            weight = newWeight.toString().toDouble()
        }

        mDialogView.btn_dialog_cancel.setOnClickListener {
            mAlertDialog.dismiss()
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
        view?.tv_weight?.text = text
    }
}