package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.ActivityMainBinding
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirebaseSource
import com.ilaydaberna.imageprocessingbaseddietapp.util.startCameraActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private var backPressedOnce = false
    val currentUser: FirebaseUser? = FirebaseSource().getAuth().currentUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupViews()

        binding.btnAddMeal.setOnClickListener {
            this.startCameraActivity()
        }
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
}
