package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.ActivityLoginBinding
import com.ilaydaberna.imageprocessingbaseddietapp.ui.base.BaseActivity
import com.ilaydaberna.imageprocessingbaseddietapp.util.startHomeActivity
import io.reactivex.internal.operators.maybe.MaybeToPublisher.instance
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class LoginActivity : BaseActivity(), KodeinAware {
    //TODO: https://www.simplifiedcoding.net/firebase-mvvm-example/
    private lateinit var binding: ActivityLoginBinding

    override val kodein: Kodein by kodein()
    private val factory : AuthViewModelFactory by instance()

    private lateinit var viewModel: AuthViewModel

    override fun initViewBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        initViewBinding()
        binding.viewModel = this.viewModel
        viewModel.authListener = this


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
        Toast.makeText(applicationContext, "Success", Toast.LENGTH_LONG).show()
    }

    override fun onFailure(message: String) {
        binding.progressbar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        viewModel.user?.let {
            startHomeActivity()
        }
    }
}