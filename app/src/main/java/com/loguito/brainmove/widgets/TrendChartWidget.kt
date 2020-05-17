package com.loguito.brainmove.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.toAppStyle
import com.loguito.brainmove.formatters.WeightFormatter
import com.loguito.brainmove.models.remote.Measurement
import kotlinx.android.synthetic.main.widget_trend_chart.view.*

class TrendChartWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var measurements: ArrayList<Measurement> = ArrayList()
        set(value) {
            field = value
            val values: ArrayList<Entry> = ArrayList()
            for ((index, value) in value.withIndex()) {
                values.add(Entry((index + 1).toFloat(), value.measure))
            }
            val set1 = LineDataSet(values, "")
            set1.toAppStyle(context)
            val data = LineData(set1)
            data.setValueFormatter(WeightFormatter())
            data.toAppStyle(context)
            lineChart.toAppStyle()
            lineChart.data = data
        }

    var trendTitle: String = String()
        set(value) {
            field = value
            measurementNameTextView.text = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_trend_chart, this, true)

        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {}

            override fun onValueSelected(entry: Entry?, h: Highlight?) {
                titleContainer.visibility = View.VISIBLE
                titleTextView.text = String.format("%s kg", entry?.y.toString())
                detailTextView.text = measurements[entry?.x?.toInt() ?: 0].date
            }
        })
    }
}