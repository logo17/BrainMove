package com.loguito.brainmove.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.UserDetailPagerAdapter
import com.loguito.brainmove.models.remote.User
import kotlinx.android.synthetic.main.fragment_user_detail.*

class UserDetailFragment : Fragment() {
    private val args: UserDetailFragmentArgs by navArgs()

    private lateinit var viewPagerAdapter: UserDetailPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(args.user)
    }

    private fun initViews(user: User) {
        nameTextView.text = user.fullName
        viewPagerAdapter = UserDetailPagerAdapter(this, user)
        pager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = getString(
                when (position) {
                    0 -> R.string.measurements_bottom_text
                    1 -> R.string.trends_bottom_text
                    else -> R.string.plans_bottom_text
                }
            )
        }.attach()
    }
}