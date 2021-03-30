package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.MainActivity

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        return view
    }
}
