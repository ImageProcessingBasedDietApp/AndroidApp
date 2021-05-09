package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FirestoreSource {
        fun setUser(currentUser: FirebaseUser){
            if (currentUser != null) {
                if (currentUser.uid != null) {
                    val db = Firebase.firestore
                    val docRef = db.collection("Users").document(currentUser.uid)
                    val task: Task<DocumentSnapshot> = docRef.get()
                    val document: DocumentSnapshot = Tasks.await(task)

                    if (document != null) {

                        val UID = document.data?.get("uid") as String
                        val email = document.data?.get("email") as String
                        val name = document.data?.get("name") as String
                        val photoUrl = document.data?.get("photoUrl") as String
                        val gender = document.data?.get("gender") as String
                        val birthdate = document.data?.get("birthdate") as Timestamp
                        val height = (document.data?.get("height") as Number).toInt()
                        val weight = (document.data?.get("weight") as Number).toFloat()
                        val goalWeight = (document.data?.get("goalWeight") as Number).toFloat()
                        val goalWater = (document.data?.get("goalWater") as Number).toInt()
                        val goalCoffee = (document.data?.get("goalCoffee") as Number).toInt()
                        val goalTea = (document.data?.get("goalTea") as Number).toInt()
                        val goalStep = (document.data?.get("goalStep") as Number).toInt()
                        val isNotification = document.data?.get("notification") as Boolean

                        UserInfo.user.set(User(UID, email, name, photoUrl, gender,
                            birthdate, height, weight, goalWeight, goalWater, goalCoffee,
                            goalTea, goalStep, isNotification))

                    } else {
                        Log.i("TAG", "No such document")
                    }

                }
            }
        }

    fun uploadPhotoToStorageUri(image: Uri?) {
        val user = UserInfo.user.get()
        if (user != null) {
            val storage = FirebaseStorage.getInstance()
            val storageReference = storage.reference
            val imageRef = storageReference.child("profile_picture/" + user.UID)
            if (image != null) {
                imageRef.putFile(image).addOnSuccessListener {
                    Log.i("Storage", "Basarili!")
                }.addOnFailureListener{
                    Log.i("Storage", "Basarisiz!")
                }
            }
        }
    }

    fun uploadPhotoToStorageBitmap(image: ByteArray) {
        val user = UserInfo.user.get()
        if (user != null) {
            val storage = FirebaseStorage.getInstance()
            val storageReference = storage.reference
            val imageRef = storageReference.child("profile_picture/" + user.UID)
            if (image != null) {
                var uploadTask = imageRef.putBytes(image)
                uploadTask.addOnSuccessListener {
                    Log.i("Storage", "Basarili!")
                }.addOnFailureListener{
                    Log.i("Storage", "Basarisiz!")
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
            if (currentUser != null) {
                if (currentUser.uid != null) {
                    val db = Firebase.firestore
                    val docRef = db.collection("Users").document(currentUser.uid)
                    docRef.set(userModel)
                }
            }
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