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
    val email: String = "",
    val photoUrl:String = "",
    val gender: Int = -1,
    val birthdate: Timestamp = Timestamp(Date(System.currentTimeMillis())),
    val height: Float = 0F,
    val weight: Float = 0F
): Parcelable
