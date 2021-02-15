package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.ActivityLoginBinding
import com.ilaydaberna.imageprocessingbaseddietapp.ui.base.BaseActivity



class LoginActivity : BaseActivity() {
    //TODO: https://www.simplifiedcoding.net/firebase-mvvm-example/
    private lateinit var binding: ActivityLoginBinding


    override fun initViewBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewBinding()

        binding.tablayout.addTab(binding.tablayout.newTab().setText("GİRİŞ"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("KAYIT"))


        binding.button2.setOnClickListener {
            Toast.makeText(applicationContext, "Firebase Register", Toast.LENGTH_LONG).show()
        }

    }

    override fun onStarted() {
        binding.progressbar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        binding.progressbar.visibility = View.GONE
    }

    override fun onFailure(message: String) {
        binding.progressbar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
      //  viewModel.user?.let {
       //     startHomeActivity()
       // }
    }
}