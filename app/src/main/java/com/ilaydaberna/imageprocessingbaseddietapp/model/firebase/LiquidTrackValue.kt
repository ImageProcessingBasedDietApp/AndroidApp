package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

data class LiquidTrackValue(
        var dailyWater: Int = 0,
        var dailyTea: Int = 0,
        var dailyCoffee: Int = 0,
        var date: String = "",
)
