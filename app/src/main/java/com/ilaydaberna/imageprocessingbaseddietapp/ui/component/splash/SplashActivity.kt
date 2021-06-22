package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirebaseSource
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirestoreSource
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login.LoginActivity


class SplashActivity : Activity() {
    private val SPLASH_DISPLAY_LENGTH = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)

        //TODO: kullanıcı login mi?
        /*if(login){
            intent = Intent(this@SplashActivity, MainActivity::class.java)
          }
          else{
            intent = Intent(this@SplashActivity, MainActivity::class.java)
          }
        */

        FirestoreSource.getFoods(handler = {
            Handler().postDelayed({ /* Create an Intent that will start the Menu-Activity. */
                this@SplashActivity.startActivity(intent)
                finish()
            }, SPLASH_DISPLAY_LENGTH.toLong())
        })
    }
}