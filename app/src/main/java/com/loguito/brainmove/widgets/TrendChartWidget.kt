package com.loguito.brainmove.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.toAppStyle
import com.loguito.brainmove.models.remote.TrendMeasure
import kotlinx.android.synthetic.main.widget_trend_chart.view.*
import java.text.DecimalFormat

class TrendChartWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var trendMeasures: List<TrendMeasure> = ArrayList()
        set(value) {
            field = value
            val values: ArrayList<Entry> = ArrayList()
            for ((index, measureValue) in value.withIndex()) {
                values.add(Entry((index + 1).toFloat(), measureValue.measure.toFloat(), null, measureValue.suffix))
            }
            val set1 = LineDataSet(values, "")
            set1.toAppStyle(context)
            val data = LineData(set1)
            data.toAppStyle(context)
            lineChart.toAppStyle()
            lineChart.data = data
            lineChart.xAxis.spaceMax = 0.1f
            lineChart.invalidate()
            titleContainer.visibility = View.INVISIBLE
        }

    var valueFormatter: ValueFormatter? = null
        set(value) {
            field = value
            lineChart.data.setValueFormatter(value)
            lineChart.invalidate()
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
                val formatter = DecimalFormat("#.##")
                titleTextView.text = String.format("%s%s", formatter.format(entry?.y?.toDouble()), entry?.data.toString())
                detailTextView.text = trendMeasures[entry?.x?.toInt()?.minus(1) ?: 0].date
            }
        })
    }
}