package com.loguito.brainmove.ext

import android.view.View
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_main.*

fun FragmentActivity.showLoadingSpinner(message: String) {
    loadingSpinner.visibility = View.VISIBLE
    loadingSpinner.setLoadingMessage(message)
}

fun FragmentActivity.hideLoadingSpinner() {
    loadingSpinner.visibility = View.GONE
}