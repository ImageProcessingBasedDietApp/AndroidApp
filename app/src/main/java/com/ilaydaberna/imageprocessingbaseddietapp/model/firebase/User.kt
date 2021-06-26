package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

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
    var isNotification: Boolean = false,
    var dailyCalorie: Int = 0,
    var dailyCarbohydrate: Int = 0,
    var dailyProtein: Int = 0,
    var dailyFat: Int = 0
)

