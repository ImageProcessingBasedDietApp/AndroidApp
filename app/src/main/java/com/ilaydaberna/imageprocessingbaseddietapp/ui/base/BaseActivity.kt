package com.ilaydaberna.imageprocessingbaseddietapp.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login.AuthListener
import org.kodein.di.KodeinAware

abstract class BaseActivity : AppCompatActivity(), AuthListener {

    protected abstract fun initViewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
    }

}