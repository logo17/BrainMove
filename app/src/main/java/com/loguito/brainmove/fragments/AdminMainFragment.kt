package com.loguito.brainmove.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.dpToPx
import kotlinx.android.synthetic.main.fragment_admin_main.*

class AdminMainFragment : Fragment() {

    private lateinit var layoutListener: ViewTreeObserver.OnGlobalLayoutListener

    override fun onResume() {
        super.onResume()
        addKeyboardDetectListener()
    }

    override fun onStop() {
        super.onStop()
        val topView = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
        topView.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTranslationZ(requireView(), 100f)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment?

        navHostFragment?.let {
            NavigationUI.setupWithNavController(
                bottomNavigationView,
                it.navController
            )
        }
    }

    private fun addKeyboardDetectListener() {
        val topView = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
        layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val heightDifference = topView.rootView.height - topView.height
            if (heightDifference > dpToPx(requireContext(), 200F)) {
                bottomNavigationView.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
            }
        }
        topView.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
    }
}