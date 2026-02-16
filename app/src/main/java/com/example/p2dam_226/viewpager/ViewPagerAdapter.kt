package com.example.p2dam_226.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.p2dam_226.fragments.FavFragment
import com.example.p2dam_226.fragments.ListFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ListFragment()
        else -> FavFragment()
    }
}
