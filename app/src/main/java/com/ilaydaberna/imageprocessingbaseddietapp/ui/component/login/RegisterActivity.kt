package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirebaseSource
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirestoreSource
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.User
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.UserInfo
import com.ilaydaberna.imageprocessingbaseddietapp.util.isNameSurnameValid
import com.ilaydaberna.imageprocessingbaseddietapp.util.startHomeActivity
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.dialog_enter_int_value.view.*
import kotlinx.android.synthetic.main.dialog_enter_string_value.view.*
import kotlinx.android.synthetic.main.dialog_enter_weight.view.*
import kotlinx.android.synthetic.main.dialog_enter_weight.view.btn_dialog_save
import java.util.*

class RegisterActivity : AppCompatActivity() {

    var nameSurname: String = ""
    var weight:Double = 0.0
    var height:Int = 0
    var goalWeight:Float = 0F
    var goalWater:Int = 0
    var goalCoffee:Int = 0
    var goalTea:Int = 0
    var goalStep:Int = 0
    var gender:String = ""
    var birthdate:Long = 0


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val textViewNameSurname = findViewById<TextView>(R.id.tv_name_surname)

        iv_woman.setOnClickListener {
            iv_woman.setImageDrawable(resources.getDrawable(R.drawable.icon_woman_selected))
            iv_man.setImageDrawable(resources.getDrawable(R.drawable.icon_man))
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_enter_string_value, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            mDialogView.btn_dialog_name_surname_save.setOnClickListener {
                if(mDialogView.et_name_surname.text.isEmpty()){
                    mDialogView.et_name_surname.setError("Bu alan boş bırakılamaz")
                }
                else if(mDialogView.et_name_surname.text.toString().isNameSurnameValid()){
                    nameSurname = mDialogView.et_name_surname.text.toString()
                    gender = "Kadın"
                    tv_name_surname.text = nameSurname.toUpperCase()
                    mAlertDialog.dismiss()

                }
                else{
                    mDialogView.et_name_surname.setError("Geçerli bir isim soyisim giriniz")
                }
            }
        }

        iv_man.setOnClickListener {
            iv_man.setImageDrawable(resources.getDrawable(R.drawable.icon_man_selected))
            iv_woman.setImageDrawable(resources.getDrawable(R.drawable.icon_woman))
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_enter_string_value, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.btn_dialog_name_surname_save.setOnClickListener {
                if(mDialogView.et_name_surname.text.isEmpty()){
                    mDialogView.et_name_surname.setError("Bu alan boş bırakılamaz")
                }
                else if(mDialogView.et_name_surname.text.toString().isNameSurnameValid()){
                    nameSurname = mDialogView.et_name_surname.text.toString()
                    gender = "Erkek"
                    tv_name_surname.text = nameSurname.toUpperCase()
                    mAlertDialog.dismiss()

                }
                else{
                    mDialogView.et_name_surname.setError("Geçerli bir isim soyisim giriniz")
                }


            }
        }

        iv_weight.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_enter_weight, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.np_dialog_weight_int.maxValue = 597
            mDialogView.np_dialog_weight_int.minValue = 0
            mDialogView.np_dialog_weight_int.value = 65
            var decimal = weight - weight.toInt()
            decimal *= 10

            mDialogView.np_dialog_weight_decimal.maxValue = 9
            mDialogView.np_dialog_weight_decimal.minValue = 0

            mDialogView.np_dialog_weight_decimal.value = decimal.toInt()
            mDialogView.btn_dialog_save.setOnClickListener {
                mAlertDialog.dismiss()
                var newWeight = mDialogView.np_dialog_weight_int.value.toString() + "." + mDialogView.np_dialog_weight_decimal.value.toString()
                weight = newWeight.toDouble()
                tv_weight.text = newWeight + " kg"
            }
        }

        iv_height.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_enter_int_value, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.np_dialog_int.maxValue = 251
            mDialogView.np_dialog_int.minValue = 73
            mDialogView.np_dialog_int.value = 160
            mDialogView.tv_dialog_header_int.text = "Lütfen boyunuzu seçiniz"


            mDialogView.btn_dialog_save.setOnClickListener {
                mAlertDialog.dismiss()
                height = mDialogView.np_dialog_int.value
                var newHeight = mDialogView.np_dialog_int.value.toString() + " cm"
                tv_height.text = newHeight
            }
        }

        iv_birthdate.setOnClickListener {
            val c = Calendar.getInstance()
            c.add(Calendar.YEAR, -15)
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog =
                    this?.let { it1 ->
                        DatePickerDialog(it1, DatePickerDialog.OnDateSetListener{ mView, mYear, mMonth, mDay ->
                            tv_birthdate.setText(""+ mDay +"/" +( mMonth+1) + "/"+ mYear)
                            val calendar = Calendar.getInstance()
                            calendar.set(mYear, mMonth, mDay)
                            val date: Date = calendar.getTime()
                            val timestamp = date.time
                            birthdate = timestamp
                        }, year, month, day)
                    }
            datePickerDialog?.show()
        }

        button_register_continue.setOnClickListener {
            layout_first_step.visibility = View.GONE
            layout_second_step.visibility = View.VISIBLE
        }

        iv_goal_weight.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_enter_weight, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            mDialogView.np_dialog_weight_int.maxValue = 597
            mDialogView.np_dialog_weight_int.minValue = 0
            mDialogView.np_dialog_weight_int.value = 65
            var decimal = goalWeight - goalWeight.toInt()
            decimal *= 10
            mDialogView.np_dialog_weight_decimal.maxValue = 9
            mDialogView.np_dialog_weight_decimal.minValue = 0
            mDialogView.np_dialog_weight_decimal.value = 0

            mDialogView.btn_dialog_save.setOnClickListener {
                mAlertDialog.dismiss()
                var newGoalWeight = mDialogView.np_dialog_weight_int.value.toString() + "." + mDialogView.np_dialog_weight_decimal.value.toString()
                goalWeight = newGoalWeight.toString().toFloat()
                tv_goal_weight.text = newGoalWeight + " kg"
            }
        }

        iv_goal_water.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_enter_int_value, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.tv_dialog_header_int.text = "Lütfen Hedef Su Miktarını Seçiniz"
            mDialogView.np_dialog_int.maxValue = 20
            mDialogView.np_dialog_int.minValue = 0
            mDialogView.np_dialog_int.value = goalWater

            mDialogView.btn_dialog_save.setOnClickListener {
                mAlertDialog.dismiss()
                goalWater = mDialogView.np_dialog_int.value
                var newGoalWater = mDialogView.np_dialog_int.value.toString() + " bardak"
                tv_goal_water.text = newGoalWater
            }
        }

        iv_goal_coffee.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_enter_int_value, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.tv_dialog_header_int.text = "Lütfen Hedef Kahve Miktarını Seçiniz"
            mDialogView.np_dialog_int.maxValue = 20
            mDialogView.np_dialog_int.minValue = 0
            mDialogView.np_dialog_int.value = goalCoffee

            mDialogView.btn_dialog_save.setOnClickListener {
                mAlertDialog.dismiss()
                goalCoffee = mDialogView.np_dialog_int.value
                var newGoalCoffee = mDialogView.np_dialog_int.value.toString() + " fincan"
                tv_goal_coffee.text = newGoalCoffee
            }
        }

        iv_goal_tea.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_enter_int_value, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.tv_dialog_header_int.text = "Lütfen Hedef Çay Miktarını Seçiniz"
            mDialogView.np_dialog_int.maxValue = 20
            mDialogView.np_dialog_int.minValue = 0
            mDialogView.np_dialog_int.value = goalTea

            mDialogView.btn_dialog_save.setOnClickListener {
                mAlertDialog.dismiss()
                goalTea = mDialogView.np_dialog_int.value
                var newGoalTea = mDialogView.np_dialog_int.value.toString() + " çay bardağı"
                tv_goal_tea.text = newGoalTea
            }
        }

        iv_goal_step.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_enter_int_value, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            val stepNums = arrayOf("0","1000", "2000", "3000", "4000", "5000", "6000", "7000","8000","9000","10000","11000","12000", "13000", "14000", "15000")
            mDialogView.tv_dialog_header_int.text = "Lütfen Hedef Adım Sayısını Seçiniz"
            mDialogView.np_dialog_int.minValue = 0
            mDialogView.np_dialog_int.maxValue = stepNums.size -1
            mDialogView.np_dialog_int.displayedValues = stepNums
            mDialogView.np_dialog_int.value = goalStep


            mDialogView.btn_dialog_save.setOnClickListener {
                mAlertDialog.dismiss()
                goalStep = mDialogView.np_dialog_int.value
                var newGoalStep = (mDialogView.np_dialog_int.value * 1000).toString() + " adım"
                tv_goal_step.text = newGoalStep
            }
        }

        button_register_save.setOnClickListener {
            //TODO: Firebase kaydet
            val currentUser: FirebaseUser? = FirebaseSource().getAuth().currentUser
            Thread(Runnable {
                UserInfo.user.set(
                    currentUser?.uid?.let { it1 ->
                        currentUser?.email?.let { it2 ->
                            User(
                                it1, it2, nameSurname, "", gender, birthdate, height, weight,
                                goalWeight, goalWater, goalCoffee, goalTea, goalStep, false )
                        }
                    }
                )
            }).start()

            val c = Calendar.getInstance()
            c.set(Calendar.HOUR, 0)
            c.set(Calendar.MINUTE, 0)
            c.set(Calendar.SECOND, 0)
            c.set(Calendar.MILLISECOND, 0)
            val d: Date = c.getTime()
            val timestamp: Long = d.getTime()

            var user = UserInfo.user.get()
            if (currentUser != null) {
                Thread.sleep(1000)
                UserInfo.user.get()?.let { it1 -> FirestoreSource().saveUser(currentUser, it1) }
                FirestoreSource().saveWeight(currentUser, weight.toDouble(), timestamp)
            }

            startHomeActivity()
        }

    }

}