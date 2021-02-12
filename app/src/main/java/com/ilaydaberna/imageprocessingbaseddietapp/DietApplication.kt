package com.ilaydaberna.imageprocessingbaseddietapp

import android.app.Application
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirebaseSource
import com.ilaydaberna.imageprocessingbaseddietapp.model.repository.UserRepository
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login.AuthViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class DietApplication  : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@DietApplication))

        bind() from singleton { FirebaseSource() }
        bind() from singleton { UserRepository(instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
       // bind() from provider { HomeViewModelFactory(instance()) }

    }
}