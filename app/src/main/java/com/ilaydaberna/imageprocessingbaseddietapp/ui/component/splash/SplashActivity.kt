package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirestoreSource
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login.LoginActivity


class SplashActivity : Activity() {
    private val SPLASH_DISPLAY_LENGTH = 4000
    private val TAG = "FirebasePushNotification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)

        FirestoreSource.getFoods(handler = {
            Handler().postDelayed({ /* Create an Intent that will start the Menu-Activity. */
                this@SplashActivity.startActivity(intent)
                finish()
            }, SPLASH_DISPLAY_LENGTH.toLong())
        })

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
        })
    }
}