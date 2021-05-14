package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

data class Liquid(
        var dailyWater: Int = 0,
        var dailyTea: Int = 0,
        var dailyCoffee: Int = 0,
        var date: Long = 0,
        var userId: String = ""
)