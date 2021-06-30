package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

data class UserSteps(
        var previousSteps: Float? = null,
        var totalSteps: Float? = null,
        var dailySteps: Float? = null,
        var date: Long = 0,
        var userId: String = ""
)
