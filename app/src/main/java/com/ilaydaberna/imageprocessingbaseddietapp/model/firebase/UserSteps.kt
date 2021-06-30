package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

data class UserSteps(
        var previousSteps: Int,
        var totalSteps: Int,
        var dailySteps: Int,
        var date: Long = 0,
        var userId: String = ""
)
