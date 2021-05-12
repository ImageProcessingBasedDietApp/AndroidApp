package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

import android.os.Parcelable
import androidx.databinding.ObservableField
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import java.util.*


data class User(
    var UID: String = "",
    var email: String = "",
    var name: String = "",
    var photoUrl:String = "",
    var gender: String = "",
    var birthdate: Long = 0,
    var height: Int = 0,
    var weight: Double = 0.0,
    var goalWeight: Float = 0F,
    var goalWater: Int = 0,
    var goalCoffee: Int = 0,
    var goalTea: Int = 0,
    var goalStep: Int = 0,
    var isNotification: Boolean = false
)

