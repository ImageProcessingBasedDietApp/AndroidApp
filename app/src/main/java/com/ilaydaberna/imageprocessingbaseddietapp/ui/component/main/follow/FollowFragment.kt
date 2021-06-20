package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.follow

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.*
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.MainActivity
import kotlinx.android.synthetic.main.dialog_enter_int_value.view.*
import kotlinx.android.synthetic.main.dialog_enter_weight.view.*
import kotlinx.android.synthetic.main.dialog_enter_weight.view.btn_dialog_save
import kotlinx.android.synthetic.main.fragment_follow.view.*
import java.text.DecimalFormat
import java.util.*


class FollowFragment : Fragment() {

    var cup_of_tea: Int = 0
    var cup_of_coffee: Int = 0
    var weight: Double = 0.0
    lateinit var weightText: String
    var newWeight: String? = ""
    val currentUser: FirebaseUser? = FirebaseSource().getAuth().currentUser
    var waterAmount: Int = 0
    var longDate: Long = 0
    var savedWater: Int? = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_follow, container, false)
        checkWater()

        FirestoreSource.getUserMealsForToday(currentUser, {}, {})

        view.btn_add_water.setOnClickListener() {
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_enter_int_value, null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.tv_dialog_header_int.text = "Lütfen Eklemek İsteediğiniz Su Miktarını Seçiniz"
            mDialogView.np_dialog_int.maxValue = 20
            mDialogView.np_dialog_int.minValue = 0
            mDialogView.np_dialog_int.value = waterAmount
            mDialogView.btn_dialog_save.setOnClickListener {
                mAlertDialog.dismiss()
                waterAmount = mDialogView.np_dialog_int.value
                this.savedWater = savedWater?.plus(waterAmount)
                this.initWaterChart()
                FirestoreSource().saveLiquid(currentUser, longDate, waterAmount, cup_of_tea, cup_of_coffee)
            }
        }

        //Handle the Weight Which is Entered
        view.iv_minus.setOnClickListener() {
            val newWeight = if ((UserInfo.user.get()?.weight ?: 0.0) >= 0.1) {
                ((UserInfo.user.get()?.weight ?: 0.0).toBigDecimal() - (0.1).toBigDecimal()).toDouble()
            } else {
                0.0
            }

            updateWeight(newWeight)
        }

        view.iv_plus.setOnClickListener() {
            val newWeight = ((UserInfo.user.get()?.weight ?: 0.0).toBigDecimal() + (0.1).toBigDecimal()).toDouble()

            updateWeight(newWeight)
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

        return view
    }

    override fun onResume() {
        super.onResume()
//        val c = Calendar.getInstance()
//        c.set(Calendar.HOUR, 0)
//        c.set(Calendar.MINUTE, 0)
//        c.set(Calendar.SECOND, 0)
//        c.set(Calendar.MILLISECOND, 0)
//        val d: Date = c.getTime()
//        val timestamp: Long = d.getTime()
//        longDate = timestamp

//        var liquid: Liquid? = null
//        Thread(Runnable {
//
//            FirestoreSource().checkWater(currentUser, longDate)
//
//            liquid = LiquidInfo.liquid.get()!!
//        }).start()
//
//        Thread.sleep(2000) // :D

    }

    override fun onStop() {
        super.onStop()

        /*if (isWeightChanged) {
            var weightDouble = weightText.toDouble()
            UserInfo.user.get()?.weight = weightDouble
            if (currentUser != null) {
                FirestoreSource().saveWeight(currentUser, weightDouble, longDate)
                UserInfo.user.get()?.let { FirestoreSource().saveUser(currentUser, it) }
            }
        }

        FirestoreSource().saveLiquid(currentUser, longDate, 0, cup_of_tea, cup_of_coffee)*/
    }

    private fun refreshUI() {
        weightText = (UserInfo.user.get()?.weight ?: 0.0).toString()
        savedWater = LiquidInfo.liquid.get()?.dailyWater

        Log.i("Saved Water", savedWater.toString())
        view?.tv_weight?.text = weightText
        this.initWaterChart()

        cup_of_coffee = LiquidInfo.liquid.get()?.dailyCoffee!!
        cup_of_tea = LiquidInfo.liquid.get()?.dailyTea!!


        view?.gl_tea?.removeAllViews()
        for(i in 1..12) {
            val iv = ImageView(context)
            if (i <= cup_of_tea) {
                iv.setImageResource(R.drawable.tea_full)
            } else {
                iv.setImageResource(R.drawable.tea_empty)
            }

            val param =  GridLayout.LayoutParams()
            param.height = 150
            param.width = 150
            param.setGravity(Gravity.CENTER)
            view?.gl_tea?.addView(iv, param)
        }

        view?.gl_coffee?.removeAllViews()
        for(i in 1..12) {
            val iv = ImageView(context)
            if (i <= cup_of_coffee) {
                iv.setImageResource(R.drawable.icon_turkish_coffee)
            } else {
                iv.setImageResource(R.drawable.icon_turkish_coffee_empty)
            }

            val param =  GridLayout.LayoutParams()
            param.height = 150
            param.width = 150
            param.setGravity(Gravity.CENTER)

            view?.gl_coffee?.addView(iv, param)

        }

        view?.amount_of_tea?.text = "Günlük içilen çay miktarı = " + cup_of_tea.toString() + " Bardak"
        view?.amount_of_coffee?.text = "Günlük içilen kahve miktarı = " + cup_of_coffee.toString() + " Bardak"
    }

    fun initWaterChart() {
        var percentage = 0
        if (savedWater != null) {
            percentage = (100 * savedWater!!) / (UserInfo.user.get()?.goalWater ?: 0)
        } else {
            savedWater = 0
        }

        if (percentage < 100) {
            view?.waterLevelView?.progressValue = percentage
            view?.waterLevelView?.centerTitle = "%$percentage"
        } else {
            view?.waterLevelView?.centerTitle = "%100"
            view?.waterLevelView?.progressValue = 100
        }
        view?.tv_water_target_amount?.text  = (UserInfo.user.get()?.goalWater ?: 0).toString() + " Bardak"
        val remaining = (UserInfo.user.get()?.goalWater ?: 0) - savedWater!!
        if (remaining > 0) {
            view?.tv_water_remaining_amount?.text = remaining.toString() + " Bardak"
        } else {
            view?.tv_water_remaining_amount?.text = "Tamamladınız"
        }
    }

    private fun checkWater() {
        (activity as MainActivity?)?.showLoading()
        FirestoreSource.checkWaterNew(currentUser, getLongTimeStamp(),
            successHandler = {
                (activity as MainActivity?)?.hideLoading()
                refreshUI()
            },
            failHandler = {
                (activity as MainActivity?)?.hideLoading()
            }
        )
    }

    private fun getLongTimeStamp() : Long {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        val d: Date = c.time
        return d.time
    }

    private fun clickedTeaMinus() {
        var tempCupOfTea = cup_of_tea;
        if (tempCupOfTea > 0) {
            tempCupOfTea -= 1
        } else {
            tempCupOfTea = 0
        }

        (activity as MainActivity?)?.showLoading()
        FirestoreSource.saveLiquidNew(currentUser, getLongTimeStamp(), waterAmount, tempCupOfTea, cup_of_coffee,
            successHandler = {
                (activity as MainActivity?)?.hideLoading()
                cup_of_tea = tempCupOfTea
                val v = view?.gl_tea?.getChildAt(cup_of_tea) as ImageView
                v.setImageResource(R.drawable.tea_empty)
                view?.amount_of_tea?.text = "Günlük içilen çay miktarı = " + cup_of_tea.toString() + " Bardak"
            },
            failHandler = {
                (activity as MainActivity?)?.hideLoading()
            }
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun clickedTeaPlus() {
        var tempCupOfTea = cup_of_tea;
        if (tempCupOfTea < 12) {
            tempCupOfTea += 1
        } else {
            tempCupOfTea = 12
        }

        (activity as MainActivity?)?.showLoading()
        FirestoreSource.saveLiquidNew(currentUser, getLongTimeStamp(), waterAmount, tempCupOfTea, cup_of_coffee,
            successHandler = {
                (activity as MainActivity?)?.hideLoading()
                cup_of_tea = tempCupOfTea
                val v = view?.gl_tea?.getChildAt(cup_of_tea-1) as ImageView
                v.setImageResource(R.drawable.tea_full)
                view?.amount_of_tea?.text = "Günlük içilen çay miktarı = " + cup_of_tea.toString() + " Bardak"
            },
            failHandler = {
                (activity as MainActivity?)?.hideLoading()
            }
        )
    }

    private fun clickedCoffeeMinus() {
        var tempCupOfCoffee = cup_of_coffee;
        if (tempCupOfCoffee > 0) {
            tempCupOfCoffee -= 1
        } else {
            tempCupOfCoffee = 0
        }

        (activity as MainActivity?)?.showLoading()
        FirestoreSource.saveLiquidNew(currentUser, getLongTimeStamp(), waterAmount, cup_of_tea, tempCupOfCoffee,
            successHandler = {
                (activity as MainActivity?)?.hideLoading()
                cup_of_coffee = tempCupOfCoffee
                val v = view?.gl_coffee?.getChildAt(cup_of_coffee) as ImageView
                v.setImageResource(R.drawable.icon_turkish_coffee_empty)
                view?.amount_of_coffee?.text = "Günlük içilen kahve miktarı = " + cup_of_coffee.toString() + " Bardak"
            },
            failHandler = {
                (activity as MainActivity?)?.hideLoading()
            }
        )
    }

    private fun clickedCoffeePlus() {
        var tempCupOfCoffee = cup_of_coffee;
        if (tempCupOfCoffee < 12) {
            tempCupOfCoffee += 1
        } else {
            tempCupOfCoffee = 12
        }

        (activity as MainActivity?)?.showLoading()
        FirestoreSource.saveLiquidNew(currentUser, getLongTimeStamp(), waterAmount, cup_of_tea, tempCupOfCoffee,
            successHandler = {
                (activity as MainActivity?)?.hideLoading()
                cup_of_coffee = tempCupOfCoffee
                val v = view?.gl_coffee?.getChildAt(cup_of_coffee-1) as ImageView
                v.setImageResource(R.drawable.icon_turkish_coffee)
                view?.amount_of_coffee?.text = "Günlük içilen kahve miktarı = " + cup_of_coffee.toString() + " Bardak"
            },
            failHandler = {
                (activity as MainActivity?)?.hideLoading()
            }
        )
    }

    private fun clickedWeightText() {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_enter_weight, null)
        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mDialogView.np_dialog_weight_int.maxValue = 597
        mDialogView.np_dialog_weight_int.minValue = 0
        mDialogView.np_dialog_weight_int.value = (UserInfo.user.get()?.weight ?: 0.0).toInt()
        var decimal = (UserInfo.user.get()?.weight ?: 0.0) - (UserInfo.user.get()?.weight ?: 0.0).toInt()
        decimal *= 10

        mDialogView.np_dialog_weight_decimal.maxValue = 9
        mDialogView.np_dialog_weight_decimal.minValue = 0

        mDialogView.np_dialog_weight_decimal.value = decimal.toInt()
        mDialogView.btn_dialog_save.setOnClickListener {
            mAlertDialog.dismiss()
            newWeight = mDialogView.np_dialog_weight_int.value.toString() + "." + mDialogView.np_dialog_weight_decimal.value.toString()
            view?.tv_weight?.text = newWeight
            updateWeight(newWeight.toString().toDouble())
        }
    }

    private fun updateWeight(newWeight: Double) {
        val oldWeight = (UserInfo.user.get()?.weight ?: 0.0)

        UserInfo.user.get()?.weight = newWeight

        UserInfo.user.get()?.let {
            FirestoreSource.saveUserNew(currentUser, it,
                successHandler = {
                    var text: String = ""
                    text = when {
                        (UserInfo.user.get()?.weight ?: 0.0) in 0.0..9.9 -> {
                            DecimalFormat("0.0").format((UserInfo.user.get()?.weight ?: 0.0))
                        }
                        (UserInfo.user.get()?.weight ?: 0.0) in 9.99..99.9 -> {
                            DecimalFormat("00.0").format((UserInfo.user.get()?.weight ?: 0.0))
                        }
                        else -> {
                            DecimalFormat("000.0").format((UserInfo.user.get()?.weight ?: 0.0))
                        }
                    }
                    weightText = text
                    view?.tv_weight?.text = text
                    updateWeightTracking()
                },
                failHandler = {
                    (activity as MainActivity?)?.hideLoading()
                    UserInfo.user.get()?.weight = oldWeight
                }
            )
        }
    }

    private fun updateWeightTracking() {
        FirestoreSource.saveWeightNew(currentUser, (UserInfo.user.get()?.weight ?: 0.0), getLongTimeStamp(),
            successHandler = {
                (activity as MainActivity?)?.hideLoading()
            },
            failHandler = {
                (activity as MainActivity?)?.hideLoading()
            }
        )
    }

    private fun showInformationDialog() {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_beverage_information, null)
        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
        mBuilder.show()
    }

}