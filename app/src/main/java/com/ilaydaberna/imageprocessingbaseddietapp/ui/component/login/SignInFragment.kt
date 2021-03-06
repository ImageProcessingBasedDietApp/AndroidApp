package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.FragmentSignInBinding

//Bu Fragmentlarda sadece viewModel bağlantısı var. AuthViewModele bağlı signup, signin ve forgotPassword.
class SignInFragment: Fragment() {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val model = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        binding = FragmentSignInBinding.inflate(inflater)
        binding.viewModel = model


        return binding.root
    }


}