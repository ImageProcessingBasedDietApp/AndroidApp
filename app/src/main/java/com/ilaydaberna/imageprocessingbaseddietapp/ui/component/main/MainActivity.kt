package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseUser
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.ActivityMainBinding
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.*
import com.ilaydaberna.imageprocessingbaseddietapp.util.getLongTimeStamp
import com.ilaydaberna.imageprocessingbaseddietapp.util.startCameraActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private var backPressedOnce = false
    val currentUser: FirebaseUser? = FirebaseSource().getAuth().currentUser

    private var sensorManager: SensorManager? = null

    override fun onSensorChanged(event: SensorEvent?) {
        var currentSteps = 0
        var previousSteps = UserStepsInfo.userSteps.get()?.previousSteps?: 0
        val totalSteps = event!!.values[0].toInt()
        if (previousSteps == 0) {
            previousSteps = totalSteps
        } else {
            currentSteps = totalSteps - previousSteps
        }
        UserStepsInfo.userSteps.set(currentUser?.let {
            UserSteps(
                    previousSteps,
                    totalSteps,
                    currentSteps,
                    getLongTimeStamp(),
                    it.uid
            )
        })
        FirestoreSource.createAndUpdateSteps(currentUser, previousSteps, totalSteps, getLongTimeStamp())
        Log.i("Steps", currentSteps.toString())
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)
        Thread(Runnable {
            currentUser?.let { FirestoreSource().setUser(it) }
        }).start()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        Thread.sleep(1000)
        var user = UserInfo.user.get()
        setupViews()


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        FirestoreSource.checkUserStep(currentUser, getLongTimeStamp(), successHandler = {
            val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            if (stepSensor != null) {
                sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
            } else {
                Log.i("StepCounter", "No sensor detected on this device.")
            }
        })

        binding.btnAddMeal.setOnClickListener {
            this.startCameraActivity()
        }

    }

    fun hideLoading(){
        binding.layoutLoading.visibility = View.GONE
        binding.btnAddMeal.isClickable = true
    }

    fun showLoading(){
        binding.layoutLoading.visibility = View.VISIBLE
        binding.btnAddMeal.isClickable = false
    }

    fun setupViews() {
        var navHostFragment = supportFragmentManager.findFragmentById(R.id.fragNavHost) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)
    }

    override fun onBackPressed() {
        if (navController.graph.startDestination == navController.currentDestination?.id)
        {
            if (backPressedOnce)
            {
                super.onBackPressed()
                return
            }

            backPressedOnce = true
            Handler().postDelayed(2000) {
                backPressedOnce = false
            }
        }
        else {
            super.onBackPressed()
        }
    }
}
