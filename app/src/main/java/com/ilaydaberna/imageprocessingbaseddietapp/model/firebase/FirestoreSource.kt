package com.ilaydaberna.imageprocessingbaseddietapp.model.firebase

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

    fun saveUser(currentUser: FirebaseUser?, userModel: User) {
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

    companion object {

        fun getFoods(handler: (() -> Unit)? = null) {
            val foods = Firebase.firestore.collection("Foods")
                    .get()
                    .addOnSuccessListener {
                        result ->
                        var foodList : ArrayList<Food> = arrayListOf()
                        for (document in result) {
                            foodList.add(Gson().fromJson(Gson().toJson(document.data), Food::class.java))
                            Log.d(TAG, "${document.id} => ${document.data}")
                        }
                        FoodSingleton.food.set(foodList)
                        handler?.invoke()
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                        handler?.invoke()
                    }
        }

        fun updateUserMealForToday (currentUser: FirebaseUser?, userMeal: UserMeals.Meal, mealType: String, successHandler: () -> Unit){
            var simpleDateFormat = SimpleDateFormat("ddMMyyyy")
            val todayStr: String  = simpleDateFormat.format(Date(System.currentTimeMillis()))
            if(currentUser != null){
                val db = Firebase.firestore
                val docRef = db.collection("Meals")
                        .document(currentUser.uid)
                        .collection(todayStr)
                        .document(mealType)
                        .update(mapOf(
                                "totalCalorie" to (userMeal.totalCalorie ?: 0),
                                "totalCarbohydrate" to (userMeal.totalCarbohydrate ?: 0),
                                "totalFat" to (userMeal.totalFat ?: 0),
                                "totalProtein" to (userMeal.totalProtein ?: 0),
                                "contents" to (userMeal.contents ?: arrayListOf())
                        ))
                        .addOnSuccessListener {
                            successHandler.invoke()
                            Log.i("updateUserMealsForToday", "Success")
                        }
                        .addOnFailureListener {
                            Log.i("updateUserMealsForToday", "Fail")
                        }
            }
        }

        fun getUserMeal(currentUser: FirebaseUser?, mealType: String, successHandler: (UserMeals.Meal) -> Unit, failHandler: () -> Unit){
            var simpleDateFormat = SimpleDateFormat("ddMMyyyy")
            val todayStr: String  = simpleDateFormat.format(Date(System.currentTimeMillis()))
            if(currentUser != null) {
                if (currentUser.uid != null) {
                    val db = Firebase.firestore
                    val docRef = db.collection("Meals")
                            .document(currentUser.uid)
                            .collection(todayStr)
                            .document(mealType)
                            .get()
                            .addOnSuccessListener{
                                val meal = UserMeals.getMeals(it)
                                successHandler(meal)
                            }
                            .addOnFailureListener {
                                failHandler()
                            }

                }
            }
        }

        fun getUserMealsForToday(currentUser: FirebaseUser?, successHandler: (UserMeals) -> Unit, failHandler: () -> Unit){
            var simpleDateFormat = SimpleDateFormat("ddMMyyyy")
            val todayStr: String  = simpleDateFormat.format(Date(System.currentTimeMillis()))

            if(currentUser != null) {
                if(currentUser.uid != null) {
                    val db = Firebase.firestore
                    val docRef = db.collection("Meals")
                        .document(currentUser.uid)
                        .collection(todayStr)
                        .get()
                        .addOnSuccessListener {
                            if(!it.isEmpty){
                                val userMeals = UserMeals (
                                    //Breakfast
                                    UserMeals.getMeals(it.documents[0]),
                                    //Lunch
                                    UserMeals.getMeals(it.documents[2]),
                                    //Dinner
                                    UserMeals.getMeals(it.documents[1]),
                                    //Snacks
                                    UserMeals.getMeals(it.documents[3]),
                                )
                                successHandler(userMeals)
                                Log.i("getUserMealsForToday", "Success")
                            }
                            else {
                                val newUserMeals = UserMeals(
                                        //Breakfast
                                        UserMeals.getEmptyMeals(),
                                        //Lunch
                                        UserMeals.getEmptyMeals(),
                                        //Dinner
                                        UserMeals.getEmptyMeals(),
                                        //Snacks
                                        UserMeals.getEmptyMeals()
                                )
                                db.collection("Meals").document(currentUser.uid)
                                        .collection(todayStr)
                                        .document("Breakfast")
                                        .set(newUserMeals.breakfast!!)
                                db.collection("Meals").document(currentUser.uid)
                                        .collection(todayStr)
                                        .document("Dinner")
                                        .set(newUserMeals.dinner!!)
                                db.collection("Meals").document(currentUser.uid)
                                        .collection(todayStr)
                                        .document("Lunch")
                                        .set(newUserMeals.lunch!!)
                                db.collection("Meals").document(currentUser.uid)
                                        .collection(todayStr)
                                        .document("Snacks")
                                        .set(newUserMeals.snacks!!)

                                successHandler(newUserMeals)
                                Log.i("getUserMealsForToday", "Fail")
                            }
                        }
                        .addOnFailureListener {
                            //TODO boş model oluşturup gönder
                            Log.i("getUserMealsForToday", "Fail")
                        }
                }
            }
        }

        fun saveUserNew(currentUser: FirebaseUser?, userModel: User, successHandler: () -> Unit, failHandler: () -> Unit) {
            if (currentUser != null) {
                if (currentUser.uid != null) {
                    val db = Firebase.firestore
                    val docRef = db.collection("Users").document(currentUser.uid)
                    docRef.set(userModel)
                        .addOnSuccessListener {
                            successHandler.invoke()
                        }
                        .addOnFailureListener {
                            failHandler.invoke()
                        }
                }
            }
        }

        fun saveWeightNew(currentUser: FirebaseUser?, weight: Double, date: Long,
                          successHandler: () -> Unit, failHandler: () -> Unit) {

            if (currentUser != null) {
                var weight = hashMapOf(
                    "date" to date,
                    "userID" to currentUser.uid,
                    "weightMeasure" to weight
                )
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
                                        successHandler.invoke()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.i(TAG, "Error adding document", e)
                                        failHandler.invoke()
                                    }
                            } else {
                                for (document in documents) {
                                    docRef.document(document.id).set(weight)
                                        .addOnSuccessListener { documentReference ->
                                            Log.i(TAG, "DocumentSnapshot written with ID: ")
                                            successHandler.invoke()
                                        }
                                        .addOnFailureListener { e ->
                                            Log.i(TAG, "Error adding document", e)
                                            failHandler.invoke()
                                        }
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.i(TAG, "Error getting documents: ", exception)
                            failHandler.invoke()
                        }
                }
            }
        }

        fun saveLiquidNew(currentUser: FirebaseUser?, date: Long, waterAmount: Int,
                          teaAmount: Int, coffeeAmount: Int, successHandler: () -> Unit, failHandler: () -> Unit) {
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
                                        successHandler.invoke()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.i(TAG, "Error adding document", e)
                                        failHandler.invoke()
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
                                        .addOnSuccessListener {
                                            successHandler.invoke()
                                        }
                                        .addOnFailureListener {
                                            failHandler.invoke()
                                        }
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.i(TAG, "Error getting documents: ", exception)
                            failHandler.invoke()
                        }
                }
            }
        }


        fun checkWaterNew(currentUser: FirebaseUser?, date: Long, successHandler: () -> Unit, failHandler: () -> Unit) {
            val liquid = LiquidInfo.liquid.get()
            if (currentUser != null) {
                if (currentUser.uid != null) {
                    val db = Firebase.firestore
                    val docRef = db.collection("LiquidTracking")
                    val query = docRef.whereEqualTo("userID", currentUser.uid)
                        .whereEqualTo("date", date)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                val dailyWater = document.data["dailyWater"].toString().toInt()
                                val dailyCoffee = document.data["dailyCoffee"].toString().toInt()
                                val dailyTea = document.data["dailyTea"].toString().toInt()
                                val date = document.data["date"].toString().toLong()
                                val userId = document.data["userID"].toString()
                                LiquidInfo.liquid.set(Liquid(dailyWater, dailyTea, dailyCoffee, date, userId))
                                successHandler.invoke()
                            }
                            if(documents.isEmpty){
                                this.initLiquidNew(currentUser, date, successHandler, failHandler)
                            }
                        }
                }
            }
        }

        fun initLiquidNew(currentUser: FirebaseUser?, date: Long, successHandler: () -> Unit, failHandler: () -> Unit) {
            if (currentUser != null) {
                if (currentUser.uid != null) {
                    val db = Firebase.firestore
                    val docRef = db.collection("LiquidTracking")
                    val liquid = hashMapOf(
                        "dailyWater" to 0,
                        "dailyCoffee" to 0,
                        "dailyTea" to 0,
                        "date" to date,
                        "userID" to currentUser.uid
                    )
                    docRef.add(liquid)
                        .addOnSuccessListener { documentReference ->
                            Log.i(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                            LiquidInfo.liquid.set(currentUser?.uid?.let { Liquid(0,0,0, date, it) })
                            successHandler.invoke()
                        }
                        .addOnFailureListener { e ->
                            Log.i(TAG, "Error adding document", e)
                            // TODO: will be refactor
                            LiquidInfo.liquid.set(currentUser?.uid?.let { Liquid(0,0,0, date, it) })
                            successHandler.invoke()
                        }
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
                            "Tabak"
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

}