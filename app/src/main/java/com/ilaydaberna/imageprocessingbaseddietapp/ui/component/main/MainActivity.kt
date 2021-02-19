package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.ui.base.BaseActivity
import org.kodein.di.Kodein

class MainActivity : BaseActivity() {


    override fun initViewBinding() {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


}