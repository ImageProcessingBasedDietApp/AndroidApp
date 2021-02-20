package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login

//Bu interface Login Activity'de implement ediliyor. Bu AuthViewModel ile arasında haberleşme köprüsü gibi düşün
//AuthviewModelde başarılı giriş olunca Loginde AuthListener'ın fonksiyonu ile yakalanıyor

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
    fun forgotPassword()
    fun onSuccessSendEmail(message: String)
}