package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

data class UserSteps(
        var previousSteps: Float,
        var totalSteps: Float,
        var dailySteps: Float,
        var date: Long = 0,
        var userId: String = ""
)
