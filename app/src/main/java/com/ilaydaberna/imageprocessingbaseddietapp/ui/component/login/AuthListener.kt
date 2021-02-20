package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
    fun forgotPassword()
    fun onSuccessSendEmail(message: String)

}