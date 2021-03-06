package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main

import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.ActivityMainBinding
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirebaseSource
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirestoreSource
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.User
import com.ilaydaberna.imageprocessingbaseddietapp.model.repository.UserRepository
import com.ilaydaberna.imageprocessingbaseddietapp.util.startCameraActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.UserInfo


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private var backPressedOnce = false
    val currentUser: FirebaseUser? = FirebaseSource().getAuth().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)
        Thread(Runnable {
            currentUser?.let { FirestoreSource().setUser(it)}
        }).start()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        Thread.sleep(1000)
        var user = UserInfo.user.get()
        setupViews()

        binding.btnAddMeal.setOnClickListener {
            this.startCameraActivity()


        }

    }

    public fun hideBtnAddMeal(){
        binding.btnAddMeal.visibility = View.GONE
    }

    public fun showBtnAddMeal(){
        binding.btnAddMeal.visibility = View.VISIBLE
    }

    fun setupViews() {
        var navHostFragment = supportFragmentManager.findFragmentById(R.id.fragNavHost) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)

        //Toolbar için
        //var appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.followFragment, R.id.chartFragment, R.id.settingsFragment))
        //setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
    }

    fun showBottomNavigation() {
        binding.bottomNavView.visibility = View.VISIBLE
    }

    fun hideBottomNavigation() {
        binding.bottomNavView.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (navController.graph.startDestination == navController.currentDestination?.id)
        {
            if (backPressedOnce)
            {
                super.onBackPressed()
                return
            }

            backPressedOnce = true
            Handler().postDelayed(2000) {
                backPressedOnce = false
            }
        }
        else {
            super.onBackPressed()
        }
    }

    suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L) // pretend we are doing something useful here, too
        return 29
    }
}
