package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

import android.util.Log
import androidx.databinding.ObservableField
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import java.util.*
import kotlin.collections.ArrayList

class FirestoreSource {
        fun setUser(currentUser: FirebaseUser){
            if (currentUser != null) {
                if (currentUser.uid != null) {
                    val db = Firebase.firestore
                    val docRef = db.collection("Users").document("yKpzYTWawN0LxieSJME8")
                    val task: Task<DocumentSnapshot> = docRef.get()
                    val document: DocumentSnapshot = Tasks.await(task)

                    if (document != null) {

                        val UID = document.data?.get("UID") as String
                        val email = document.data?.get("email") as String
                        val name = document.data?.get("name") as String
                        val surname = document.data?.get("surname") as String
                        val photoUrl = document.data?.get("photoURL") as String
                        val gender = document.data?.get("gender") as String
                        val birthdate = document.data?.get("birthdate") as Timestamp
                        val height = (document.data?.get("height") as Number).toFloat()
                        val weight = (document.data?.get("weight") as Number).toFloat()
                        val goalWeight = (document.data?.get("goalWeight") as Number).toFloat()
                        val goalWater = (document.data?.get("goalWater") as Number).toInt()
                        val goalCoffee = (document.data?.get("goalCoffee") as Number).toInt()
                        val goalTea = (document.data?.get("goalTea") as Number).toInt()
                        val goalStep = (document.data?.get("goalStep") as Number).toInt()
                        val isNotification = document.data?.get("isNotification") as Boolean

                        UserInfo.user.set(User(UID, email, name, surname, photoUrl, gender,
                                birthdate, height, weight, goalWeight, goalWater, goalCoffee,
                                goalTea, goalStep, isNotification))

                    } else {
                        Log.i("TAG", "No such document")
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