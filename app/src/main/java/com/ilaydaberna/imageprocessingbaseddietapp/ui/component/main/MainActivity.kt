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
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = UserStepsInfo.userSteps.get()?.previousSteps

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            totalSteps = event!!.values[0]
            val currentSteps = totalSteps.toInt() - (previousTotalSteps ?: totalSteps).toInt()
            UserStepsInfo.userSteps.set(currentUser?.let {
                UserSteps(
                        previousTotalSteps ?: 0,
                        (totalSteps ?: 0) as Int,
                        currentSteps,
                        getLongTimeStamp(),
                        it?.uid
                )
            })
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor != null) {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            Log.i("StepCounter", "No sensor detected on this device.")
        }
        checkAndUpdateUserSteps()
    }

    fun checkAndUpdateUserSteps() {
        showLoading()
        FirestoreSource.createAndUpdateSteps(currentUser, (previousTotalSteps
                ?: 0).toInt(), totalSteps.toInt(), getLongTimeStamp(),
                successHandler = {
                    hideLoading()
                },
                failHandler = {
                    hideLoading()

                })
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

        checkAndUpdateUserSteps()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager


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
