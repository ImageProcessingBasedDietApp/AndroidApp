package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ilaydaberna.imageprocessingbaseddietapp.R
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.layout_breakfast.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addMealFragment)
            Toast.makeText(context, "Kahvaltı", Toast.LENGTH_SHORT).show()
        }
        view.layout_launch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addMealFragment)
            Toast.makeText(context, "Öğle Yemeği", Toast.LENGTH_SHORT).show()
        }
        view.layout_dinner.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addMealFragment)
            Toast.makeText(context, "Akşam Yemeği", Toast.LENGTH_SHORT).show()
        }
        view.layout_snacks.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addMealFragment)
            Toast.makeText(context, "Atıştırmalık", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}