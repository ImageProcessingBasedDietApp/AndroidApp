package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.FragmentForgotPasswordBinding
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.FragmentSignInBinding

//Bu Fragmentlarda sadece viewModel bağlantısı var. AuthViewModele bağlı signup, signin ve forgotPassword.
class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val model = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        binding = FragmentForgotPasswordBinding.inflate(inflater)
        binding.viewModel = model


        return binding.root
    }
}