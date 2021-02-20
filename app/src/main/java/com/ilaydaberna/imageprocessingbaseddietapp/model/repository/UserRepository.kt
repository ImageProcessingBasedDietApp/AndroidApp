package com.ilaydaberna.imageprocessingbaseddietapp.model.repository

import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirebaseSource

//Bu class ile FirebaseSource erişimi oluyor.
class UserRepository (private val firebase: FirebaseSource){
    fun login(email: String, password: String) = firebase.login(email, password)

    fun register(email: String, password: String) = firebase.register(email, password)

    fun currentUser() = firebase.currentUser()

    fun sendPasswordResetEmail(email: String) = firebase.sendPasswordResetEmail(email)

    fun logout() = firebase.logout()
}