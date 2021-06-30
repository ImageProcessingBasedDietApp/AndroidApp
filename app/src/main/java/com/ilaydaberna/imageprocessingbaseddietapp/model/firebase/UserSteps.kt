package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

data class UserSteps(
        var previousSteps: Int? = null,
        var totalSteps: Int? = null,
        var dailySteps: Int? = null,
        var date: Long = 0,
        var userId: String = ""
)
