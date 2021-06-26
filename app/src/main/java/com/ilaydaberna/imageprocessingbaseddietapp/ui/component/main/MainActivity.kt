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
import com.google.gson.Gson
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.ActivityMainBinding
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirebaseSource
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirestoreSource
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.UserInfo
import com.ilaydaberna.imageprocessingbaseddietapp.util.startCameraActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private var backPressedOnce = false
    val currentUser: FirebaseUser? = FirebaseSource().getAuth().currentUser
    lateinit var sensorManager: SensorManager

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

        binding.btnAddMeal.setOnClickListener {
            this.startCameraActivity()
        }

    }

    override fun onResume() {
        super.onResume()
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepCounterSensor.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    fun hideLoading() {
        binding.layoutLoading.visibility = View.GONE
        binding.btnAddMeal.isClickable = true
    }

    fun showLoading() {
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
        } else {
            super.onBackPressed()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i("StepCounterService", "onAccuracyChanged: Sensor: $sensor; accuracy: $accuracy")
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        sensorEvent ?: return
        Log.i("StepCounterService", "onSensorChanged: sensorEvent: ${Gson().toJson(sensorEvent)}")
        sensorEvent.values.firstOrNull()?.let {
            Log.i("StepCounterService", "Step count: $it ")
        }
    }
}
