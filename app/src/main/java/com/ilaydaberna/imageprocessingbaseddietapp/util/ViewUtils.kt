package com.ilaydaberna.imageprocessingbaseddietapp.util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.WriteBatch
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.camera.CameraActivity
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login.LoginActivity
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login.RegisterActivity
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.MainActivity
import java.text.SimpleDateFormat
import java.util.*


fun Context.startHomeActivity() =
        Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }

fun Context.startLoginActivity() =
        Intent(this, LoginActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }

fun Context.startCameraActivity() =
    Intent(this, CameraActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startRegisterActivity() =
        Intent(this, RegisterActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }

fun EditText.isEmpty() = this.text.isEmpty()

fun Fragment.alertDialog(context: Context, title: String, msg: String, positiveBtn: String, negativeBtn: String?, positive: WriteBatch.Function?){
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(msg)

    builder.setPositiveButton(positiveBtn) { dialog, which ->
        if(positive == null){
            dialog.dismiss()
        }
    }

    if(negativeBtn != null){
        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }
    }

    builder.show()

}