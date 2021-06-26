package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.splash

import android.Manifest
import android.app.Activity
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirestoreSource
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login.LoginActivity
import com.ilaydaberna.imageprocessingbaseddietapp.util.StepCounterService

private const val MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 5000

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION
            )
        } else {
            //setJobScheduler()
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    //setJobScheduler()
                } else {
                    //todo tekrar request olabilir ya da gösterme
                }
                return
            }
        }
    }

    private fun setJobScheduler() {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val componentName = ComponentName(this, StepCounterService::class.java)
        val jobInfo = JobInfo.Builder(555, componentName)
            .setPeriodic(15 * 60 * 1000) //15 * 60 * 1000
            .build()
        jobScheduler.schedule(jobInfo)
    }

}