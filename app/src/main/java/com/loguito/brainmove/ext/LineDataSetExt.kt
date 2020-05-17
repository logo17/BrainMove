package com.loguito.brainmove.ext

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.loguito.brainmove.R

fun LineDataSet.toAppStyle(context: Context) {
    this.apply {
        axisDependency = YAxis.AxisDependency.LEFT
        color = ContextCompat.getColor(context, R.color.black)
        valueTextColor = ContextCompat.getColor(context, R.color.black)
        lineWidth = 1.5f
        setDrawCircles(true)
        setDrawValues(true)
        fillAlpha = 65
        fillColor = ContextCompat.getColor(context, R.color.black)
        highLightColor = Color.rgb(244, 117, 117)
        setDrawCircleHole(true)
        mode = LineDataSet.Mode.CUBIC_BEZIER
        setDrawFilled(true)
        setCircleColor(ContextCompat.getColor(context, R.color.black))
        val fillGradient =
            ContextCompat.getDrawable(context, R.drawable.gradient_chart_drawable)
        fillDrawable = fillGradient
        setDrawHighlightIndicators(true)
        highLightColor = Color.BLACK
        setDrawHorizontalHighlightIndicator(false)
    }
}