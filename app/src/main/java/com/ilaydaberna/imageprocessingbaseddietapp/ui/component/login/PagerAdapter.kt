package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.login

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

//Bu LoginActivity'deki ViewPager'ın adapteri. Hangi fragmentta olduğunu tutuyor.
class PagerAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return SignInFragment()
            }
            1 -> {
                return SignUpFragment()
            }
            2 -> {
                return ForgotPasswordFragment()
            }

            else -> return SignInFragment()
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }


}
