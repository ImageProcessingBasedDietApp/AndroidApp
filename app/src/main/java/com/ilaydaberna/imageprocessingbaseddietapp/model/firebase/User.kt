package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(
    val UID: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = ""): Parcelable
{
    val photoUrl:String = ""
    val gender: String = ""
    val birthdate: Timestamp = Timestamp(Date(System.currentTimeMillis()))
    val height: Float = 0F
    val weight: Float = 0F
    val goalWeight: Float = 0F
    val goalWater: Int = 0
    val goalCoffee: Int = 0
    val goalTea: Int = 0
    val goalStep: Int = 0
    val isNotification: Boolean = false
}

