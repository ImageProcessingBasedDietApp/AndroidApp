package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.ActivityLoginBinding
import com.ilaydaberna.imageprocessingbaseddietapp.ui.base.BaseActivity
import com.ilaydaberna.imageprocessingbaseddietapp.util.startHomeActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class LoginActivity : BaseActivity(), KodeinAware, AuthListener{
    private lateinit var binding: ActivityLoginBinding
    override val kodein: Kodein by kodein()
    private val factory : AuthViewModelFactory by instance()
    private lateinit var viewModel: AuthViewModel

    override fun initViewBinding() {
        // https://www.simplifiedcoding.net/firebase-mvvm-example/
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = this.viewModel
        viewModel.authListener = this

    }

    //Sayfa ilk başladığında kullanıcı var ise HomeActivity'e gönderiliyor.
    override fun onStart() {
        super.onStart()
        viewModel.user?.let {
            startHomeActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewBinding()

        binding.tablayout.addTab(binding.tablayout.newTab().setText("GİRİŞ YAP"))
        binding.tablayout.addTab(binding.tablayout.newTab().setText("KAYIT OL"))

        val mAdapter = PagerAdapter(this, supportFragmentManager, 3)
        binding.viewpager.adapter = mAdapter

        binding.buttonGoogle.setOnClickListener {
            Toast.makeText(applicationContext, "Google Register", Toast.LENGTH_LONG).show()
        }

        binding.viewpager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tablayout))
        binding.tablayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewpager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    //AuthViewModelde işlem başlıyor. Progress bar görünür oluyor.
    override fun onStarted() {
        binding.progressbar.visibility = View.VISIBLE
    }

    //AuthViewModelde işlem başarılı bitiyor. Home Activity yönlendirmesi yapılıyor.
    override fun onSuccess() {
        binding.progressbar.visibility = View.GONE
        startHomeActivity()
    }

    //AuthViewModelde gotoForgotPassword fonksiyonu buna düşüyor. Şifremi unuttum bu sayede açılıyor.
    override fun forgotPassword() {
        binding.viewpager.currentItem = 2
    }

    //AuthViewModelde şifre sıfırlama fonksiyonu success durumunda buna düşüyor.
    override fun onSuccessSendEmail(message: String) {
        binding.viewpager.currentItem = 0
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    //AuthViewModeldeki tüm hatalar buna düşüyor. Hata mesajını yayınlayıp progress barı kapatıyor.
    override fun onFailure(message: String) {
        binding.progressbar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}