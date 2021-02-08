package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.ui.base.BaseActivity

class LoginActivity : BaseActivity() {

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun observeViewModel() {
        TODO("Not yet implemented")
    }

    override fun initViewBinding() {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}