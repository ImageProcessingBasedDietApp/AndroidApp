package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.FragmentSignInBinding


class SignInFragment: Fragment() {

    private lateinit var binding: FragmentSignInBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        val model = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        binding = FragmentSignInBinding.inflate(inflater)
        binding.viewModel = model


        return binding.root
    }


}