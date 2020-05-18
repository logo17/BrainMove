package com.loguito.brainmove.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.loguito.brainmove.R
import kotlinx.android.synthetic.main.widget_measurement.view.*

class MeasurementWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var measureValue: String = String()
        set(value) {
            field = value
            valueTextView.text = value
            isEnabled = true
        }

    init {
        background =
            ContextCompat.getDrawable(context, R.drawable.measurement_widget_background_selector)
        setPadding(30)
        LayoutInflater.from(context).inflate(R.layout.widget_measurement, this, true)
        attrs?.let {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.MeasurementWidget,
                defStyleAttr,
                defStyleRes
            )
            isEnabled = typedArray.getBoolean(R.styleable.MainButtonWidget_android_enabled, false)
            valueTextView.text = typedArray.getString(R.styleable.MeasurementWidget_value)
            labelTextView.text = typedArray.getString(R.styleable.MeasurementWidget_label)
            typedArray.recycle()
        }
    }
}