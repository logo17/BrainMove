package com.loguito.brainmove.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.loguito.brainmove.fragments.UserDetailsMeasuresFragment
import com.loguito.brainmove.fragments.UserDetailsPlanFragment
import com.loguito.brainmove.fragments.UserDetailsTrendsFragment
import com.loguito.brainmove.models.remote.User
import com.loguito.brainmove.utils.Constants.Companion.USER_KEY

class UserDetailPagerAdapter(fragment: Fragment, private val user: User) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> UserDetailsMeasuresFragment()
            1 -> UserDetailsTrendsFragment()
            else -> UserDetailsPlanFragment()
        }
        fragment.arguments = Bundle().apply {
            putParcelable(USER_KEY, user)
        }
        return fragment
    }
}