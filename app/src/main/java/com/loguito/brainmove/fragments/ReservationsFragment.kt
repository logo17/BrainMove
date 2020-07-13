package com.loguito.brainmove.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.ReservationsPagerAdapter
import kotlinx.android.synthetic.main.fragment_reservations.*

class ReservationsFragment : Fragment() {
    private lateinit var viewPagerAdapter: ReservationsPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reservations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        viewPagerAdapter = ReservationsPagerAdapter(this)
        pager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = getString(
                when (position) {
                    0 -> R.string.today_tab_text
                    else -> R.string.tomorrow_tab_text
                }
            )
        }.attach()
    }
}