package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.follow

import android.app.AlertDialog
import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ilaydaberna.imageprocessingbaseddietapp.R
import kotlinx.android.synthetic.main.dialog_enter_weight.view.*
import kotlinx.android.synthetic.main.fragment_follow.view.*
import java.text.DecimalFormat

class FollowFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_follow, container, false)
        var weight: Double = 49.0 //Kullanıcıdan alınan kiloya setlenecek.
        var newWeight: String? = null

        view.tv_weight.setText(weight.toString())

        view.iv_minus.setOnClickListener(){
            if(weight >= 0.1){
                weight -= 0.1
            } else {
                weight = 0.0
            }

            var text : String = ""
            text = if(weight in 0.0..9.9) {
                DecimalFormat("0.0").format(weight)
            } else if(weight in 9.9..99.9) {
                DecimalFormat("00.0").format(weight)
            } else {
                DecimalFormat("000.0").format(weight)
            }
            view.tv_weight.text = text
        }

        view.iv_plus.setOnClickListener(){
            weight += 0.1
            var text : String = ""
            text = if(weight in 0.0..9.9) {
                DecimalFormat("0.0").format(weight)
            } else if(weight in 9.99..99.9) {
                DecimalFormat("00.0").format(weight)
            } else {
                DecimalFormat("000.0").format(weight)
            }
            view.tv_weight.text = text
        }

        view.tv_weight.setOnClickListener(){
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_enter_weight, null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            mDialogView.btn_dialog_save.setOnClickListener {
                mAlertDialog.dismiss()
                newWeight = mDialogView.et_dialog_weight.text.toString()

                if(!newWeight.equals("") &&!newWeight?.contains(".")!!) {
                    newWeight = "$newWeight.0"
                }

                if(newWeight.equals("")) {
                    newWeight = "0.0"
                }

                view.tv_weight.text = newWeight
                weight = newWeight.toString().toDouble()
            }

            mDialogView.btn_dialog_cancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
        return view
    }
}