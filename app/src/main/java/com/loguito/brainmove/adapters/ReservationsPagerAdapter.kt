package com.loguito.brainmove.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.loguito.brainmove.fragments.ReservationListFragment
import com.loguito.brainmove.utils.Constants
import java.util.*

class ReservationsPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = ReservationListFragment()
        if (position == 0) {
            fragment.arguments = Bundle().apply {
                putLong(Constants.DATE_KEY, getCurrentDate().time)
            }
        } else {
            fragment.arguments = Bundle().apply {
                putLong(Constants.DATE_KEY, getTomorrowDate().time)
            }
        }
        return fragment
    }

    private fun getCurrentDate() = Date()

    private fun getTodayStartDate() : Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }

    private fun getTomorrowDate(): Date {
        val tomorrow = Calendar.getInstance()
        tomorrow.time = getTodayStartDate()
        tomorrow.add(Calendar.DATE, 1)
        return tomorrow.time
    }
}