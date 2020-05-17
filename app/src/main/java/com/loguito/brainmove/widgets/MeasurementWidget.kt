package com.loguito.brainmove.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.loguito.brainmove.R
import kotlinx.android.synthetic.main.widget_measurement.view.*

class MeasurementWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.widget_measurement, this, true)
        attrs?.let {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.MeasurementWidget,
                defStyleAttr,
                defStyleRes
            )

            valueTextView.text = typedArray.getString(R.styleable.MeasurementWidget_value)
            labelTextView.text = typedArray.getString(R.styleable.MeasurementWidget_label)
            view.visibility = if (typedArray.getBoolean(
                    R.styleable.MeasurementWidget_isDividerVisible,
                    false
                )
            ) View.VISIBLE else View.GONE
            typedArray.recycle()
        }
    }
}