package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.GoogleAuthProvider
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.ActivityLoginBinding
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.FirebaseSource
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
    private lateinit var googleSignInClient : GoogleSignInClient
    private var RC_SIGN_IN = 123

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
            signIn()
            Toast.makeText(applicationContext, "Google Register", Toast.LENGTH_LONG).show()
        }

        binding.buttonFacebook.setOnClickListener {
            Toast.makeText(applicationContext, "Facebook Register", Toast.LENGTH_LONG).show()
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

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
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
        binding.progressbar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    //AuthViewModeldeki tüm hatalar buna düşüyor. Hata mesajını yayınlayıp progress barı kapatıyor.
    override fun onFailure(message: String) {
        binding.progressbar.visibility = View.GONE
        //ister dialog koy
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun facebookBtnClicked() {
        Toast.makeText(this, "Facebook Login", Toast.LENGTH_LONG).show()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("firebaseAuthWithGoogle", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("firebaseAuthWithGoogle", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val auth = FirebaseSource().getAuth()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("firebaseAuthWithGoogle", "signInWithCredential:success")
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("firebaseAuthWithGoogle", "signInWithCredential:failure", task.exception)
                    //updateUI(null)
                }
            }
    }

}