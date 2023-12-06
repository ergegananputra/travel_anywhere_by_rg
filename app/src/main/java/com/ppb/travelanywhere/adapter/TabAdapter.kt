package com.ppb.travelanywhere.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ppb.travelanywhere.authenticate.LoginFragment
import com.ppb.travelanywhere.authenticate.RegisterFragment

class TabAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle){
    private val page = arrayOf<Fragment>(
        RegisterFragment(),
        LoginFragment(),
    )
    override fun getItemCount(): Int = page.size

    override fun createFragment(position: Int): Fragment {
        return page[position]
    }
}