package com.loguito.brainmove.ext

import android.content.Context
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.LineData
import com.loguito.brainmove.R

fun LineData.toAppStyle(context: Context) {
    this.apply {
        setValueTextSize(9f)
        setValueTextColor(ContextCompat.getColor(context, R.color.black))
    }
}