package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.ActivityLoginBinding
import com.ilaydaberna.imageprocessingbaseddietapp.ui.base.BaseActivity
import org.kodein.di.Kodein
import com.ilaydaberna.imageprocessingbaseddietapp.util.startHomeActivity
import org.kodein.di.generic.instance
import com.ilaydaberna.imageprocessingbaseddietapp.R.layout.activity_login

class LoginActivity : BaseActivity(){
//TODO: https://www.simplifiedcoding.net/firebase-mvvm-example/
    private lateinit var binding: ActivityLoginBinding
    private val factory : AuthViewModelFactory by instance()
    private lateinit var viewModel: AuthViewModel


    override fun initViewBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))

        //binding.viewModel = viewModel
        //binding.lifecycleOwner = this
        binding.button2.setOnClickListener {
            Toast.makeText(applicationContext, "Firebase Register", Toast.LENGTH_LONG).show()
        }

    }

    override fun onStarted() {
        progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        progressbar.visibility = View.GONE
    }

    override fun onFailure(message: String) {
        progressbar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override val kodein: Kodein
        get() = TODO("Not yet implemented")

    override fun onStart() {
        super.onStart()
        viewModel.user?.let {
            startHomeActivity()
        }
    }
}