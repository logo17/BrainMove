package com.loguito.brainmove.ext

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis

fun LineChart.toAppStyle() {
    this.axisRight.apply {
        setDrawGridLines(false)
        setDrawLabels(true)
        setDrawAxisLine(true)
    }
    this.axisLeft.apply {
        setDrawGridLines(false)
        setDrawLabels(false)
        setDrawAxisLine(false)
    }
    this.xAxis.apply {
        setDrawGridLines(false)
        setDrawLabels(false)
        setDrawAxisLine(true)
        position = XAxis.XAxisPosition.BOTTOM
    }
    this.setDrawGridBackground(false)
    this.setDrawBorders(false)
    this.description.isEnabled = false
    this.legend.isEnabled = false
}