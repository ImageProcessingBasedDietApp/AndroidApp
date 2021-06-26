package com.ilaydaberna.imageprocessingbaseddietapp.util

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.google.gson.Gson

class StepCounterService : JobService(), SensorEventListener {
    override fun onStartJob(jobParameters: JobParameters?): Boolean {
        Log.i("StepCounterService", "run")
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepCounterSensor?.let {
            sensorManager.registerListener(
                this@StepCounterService,
                it,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
        return true;
    }

    override fun onStopJob(jobParameters: JobParameters?): Boolean {
        return true
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