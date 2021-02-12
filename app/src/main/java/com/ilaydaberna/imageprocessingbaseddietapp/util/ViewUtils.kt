package com.ilaydaberna.imageprocessingbaseddietapp.util

import android.content.Context
import android.content.Intent
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login.LoginActivity
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.MainActivity


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