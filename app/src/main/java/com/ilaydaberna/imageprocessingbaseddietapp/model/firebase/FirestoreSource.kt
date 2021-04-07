package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

import android.util.Log
import androidx.databinding.ObservableField
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class FirestoreSource {
        fun setUser(currentUser: FirebaseUser){
            if (currentUser != null) {
                if (currentUser.uid != null) {
                    val db = Firebase.firestore
                    val docRef = db.collection("Users").document("yKpzYTWawN0LxieSJME8")
                    docRef.get()
                            .addOnSuccessListener{document ->
                                if (document != null) {
                                    User().UID = "dfghjklşi"
                                    User().email ="gmaikceduvhsıuorshb"

                                    UserInfo.user.set(User("dfghjklş","dfghjklşi"))

                                    /*
                                    user!!.name = document.data?.get("name") as String
                                    user!!.surname = document.data?.get("surname") as String
                                    user!!.photoUrl = document.data?.get("photoURL") as String
                                    user!!.gender = document.data?.get("gender") as String
                                    user!!.birthdate = document.data?.get("birthdate") as Timestamp
                                    user!!.height = (document.data?.get("height") as Number).toFloat()
                                    user!!.weight = (document.data?.get("weight") as Number).toFloat()
                                    user!!.goalWeight = (document.data?.get("goalWeight") as Number).toFloat()
                                    user!!.goalWater = (document.data?.get("goalWater") as Number).toInt()
                                    user!!.goalCoffee = (document.data?.get("goalCoffee") as Number).toInt()
                                    user!!.goalTea = (document.data?.get("goalTea") as Number).toInt()
                                    user!!.goalStep = (document.data?.get("goalStep") as Number).toInt()
                                    user!!.isNotification = document.data?.get("isNotification") as Boolean
                                    */


                                } else {
                                    Log.i("TAG", "No such document")
                                }
                            }.addOnFailureListener { exception ->
                                Log.i("TAG", "get failed with ", exception)
                            }
                }
            }
        }

        fun initUser(currentUser: FirebaseUser) {
            if (currentUser != null) {
                if (currentUser.uid != null) {
                    val db = Firebase.firestore
                    val docRef = db.collection("Users").document(currentUser.uid)
                }
            }
        }

        fun saveUser(currentUser: FirebaseUser, userModel: User) {

        }

        fun getFoods() : ArrayList<Food> {
            val foods: ArrayList<Food>? = null
            val db = Firebase.firestore
            val docRef = db.collection("Foods")
            return foods!!
        }

        fun initLiquidTracking(currentUser: FirebaseUser) {
            val date = Timestamp.now().toDate()
            val db = Firebase.firestore
            Log.i("date" , date.toString()) //??
            val docRef = db.collection("LiquidTracking").document(currentUser.uid + "-" + date.toString())
        }

}