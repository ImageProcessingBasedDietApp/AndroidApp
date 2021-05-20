package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat

class FirestoreSource {
    fun setUser(currentUser: FirebaseUser) {
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
                    val birthdate = (document.data?.get("birthdate") as Number).toLong()
                    val height = (document.data?.get("height") as Number).toInt()
                    val weight = (document.data?.get("weight") as Number).toDouble()
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
                }.addOnFailureListener {
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
                }.addOnFailureListener {
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

    fun saveWeight(currentUser: FirebaseUser, weight: Double, date: Long) {
        var weight = hashMapOf(
                "date" to date,
                "userID" to currentUser.uid,
                "weightMeasure" to weight
        )
        if (currentUser != null) {
            if (currentUser.uid != null) {
                val db = Firebase.firestore
                val docRef = db.collection("WeightTracking")
                val query = docRef.whereEqualTo("userID", currentUser.uid)
                        .whereEqualTo("date", date)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents.size() == 0) {
                                docRef.add(weight)
                                        .addOnSuccessListener { documentReference ->
                                            Log.i(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.i(TAG, "Error adding document", e)
                                        }
                            } else {
                                for (document in documents) {
                                    docRef.document(document.id).set(weight)
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.i(TAG, "Error getting documents: ", exception)
                        }
            }
        }
    }

    fun saveLiquid(currentUser: FirebaseUser?, date: Long, waterAmount: Int, teaAmount: Int, coffeeAmount: Int) {
        if (currentUser != null) {
            if (currentUser.uid != null) {
                val db = Firebase.firestore
                val docRef = db.collection("LiquidTracking")

                val query = docRef.whereEqualTo("userID", currentUser.uid)
                        .whereEqualTo("date", date)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents.size() == 0) {
                                val liquid = hashMapOf(
                                        "dailyWater" to waterAmount,
                                        "dailyCoffee" to coffeeAmount,
                                        "dailyTea" to teaAmount,
                                        "date" to date,
                                        "userID" to currentUser.uid
                                )
                                docRef.add(liquid)
                                        .addOnSuccessListener { documentReference ->
                                            Log.i(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.i(TAG, "Error adding document", e)
                                        }
                            } else {
                                for (document in documents) {
                                    val totalWater = document.data["dailyWater"].toString().toInt() + waterAmount
                                    val liquid = hashMapOf(
                                            "dailyWater" to totalWater,
                                            "dailyCoffee" to coffeeAmount,
                                            "dailyTea" to teaAmount,
                                            "date" to date,
                                            "userID" to currentUser.uid
                                    )
                                    docRef.document(document.id).set(liquid)
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.i(TAG, "Error getting documents: ", exception)
                        }
            }
        }
    }


    fun checkWater(currentUser: FirebaseUser?, date: Long) {
        val liquid = LiquidInfo.liquid.get()
        if (currentUser != null) {
            if (currentUser.uid != null) {
                val db = Firebase.firestore
                val docRef = db.collection("LiquidTracking")
                val query = docRef.whereEqualTo("userID", currentUser.uid)
                        .whereEqualTo("date", date)
                val task: Task<QuerySnapshot> = query.get()
                val documents: QuerySnapshot = Tasks.await(task)
                for (document in documents) {
                    val dailyWater = document.data["dailyWater"].toString().toInt()
                    val dailyCoffee = document.data["dailyCoffee"].toString().toInt()
                    val dailyTea = document.data["dailyTea"].toString().toInt()
                    val date = document.data["date"].toString().toLong()
                    val userId = document.data["userID"].toString()
                    LiquidInfo.liquid.set(Liquid(dailyWater, dailyTea, dailyCoffee, date, userId))
                }

            }
        }
    }

    fun updateUser(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            if (currentUser.uid != null) {
                val db = Firebase.firestore
                val docRef = db.collection("Users").document(currentUser.uid)

                if (docRef != null) {
                    UserInfo.user.get()?.let { docRef.set(it) }
                }
            }
        }
    }
    
    fun getFoods(callback: GetFoodsCallback){
        val foods = arrayListOf<Food>()
        val db = Firebase.firestore
        db.collection("Foods").get()
                .addOnSuccessListener { documents ->
                    for(document in documents){
                        foods.add(
                                Food(
                                        document.get("ID").toString().toInt(),
                                        document.get("fileName").toString(),
                                        document.get("name").toString(),
                                        document.getString("calorie")!!.toDouble(),
                                        document.getString("carbohydrate")!!.toDouble(),
                                        document.getString("fat")!!.toDouble(),
                                        document.getString("protein")!!.toDouble(),
                                        ServingType(1, "Kase") //TODO:
                            )
                        )
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                    callback.onCallback(foods)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
    }


    fun getWeightTrackingValues(currentUser: FirebaseUser, callback: GetUserWeightTrackigCallBack) {
        val userWeights = arrayListOf<WeightTrackValue>()
        val db = Firebase.firestore
        db.collection("WeightTracking")
                .whereEqualTo("userID", currentUser.uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        //Converted birthday from long to Date format.
                        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                        val dateString = simpleDateFormat.format(document.get("date"))
                        userWeights.add(
                                WeightTrackValue(
                                        String.format("%s", dateString),
                                        document.get("weightMeasure").toString().toFloat()
                                )
                        )
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                    callback.onCallback(userWeights)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)

                }
    }


    fun getLiquidTrackingValues(currentUser: FirebaseUser, callback: GetUserLiquidTrackingCallback){
        val userLiquidValues = arrayListOf<LiquidTrackValue>()
        val db = Firebase.firestore
        db.collection("LiquidTracking")
                .whereEqualTo("userID", currentUser.uid)
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents){
                        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                        val dateString = simpleDateFormat.format(document.get("date"))
                        userLiquidValues.add(LiquidTrackValue(
                                document.get("dailyWater").toString().toInt(),
                                document.get("dailyTea").toString().toInt(),
                                document.get("dailyCoffee").toString().toInt(),
                                String.format("%s", dateString)))
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                    callback.onCallback(userLiquidValues)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)

                }
    }

}