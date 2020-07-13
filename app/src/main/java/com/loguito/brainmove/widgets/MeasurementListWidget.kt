package com.loguito.brainmove.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.loguito.brainmove.R
import com.loguito.brainmove.ext.*
import com.loguito.brainmove.models.remote.Measure
import kotlinx.android.synthetic.main.widget_measurement_list.view.*

class MeasurementListWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var measure: Measure? = null
        set(value) {
            field = value
            value?.let {
                fillMeasures(it)
            }
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_measurement_list, this, true)
        orientation = VERTICAL
    }

    private fun fillMeasures(measure: Measure) {
        weightMeasurementWidget.measureValue = measure.weight.formatToKgString()
        bmiMeasurementWidget.measureValue = measure.bmi.formatToString()
        bodyFatMeasurementWidget.measureValue = measure.body_fat.formatToPercentageString()
        fatFreeBodyMeasurementWidget.measureValue = measure.fat_free_body.formatToKgString()
        subcutaneousFatMeasurementWidget.measureValue =
            measure.subcutaneous_fat.formatToPercentageString()
        visceralFatMeasurementWidget.measureValue = measure.visceral_fat.formatToString()
        bodyWaterMeasurementWidget.measureValue = measure.body_water.formatToPercentageString()
        skeletalMuscleMeasurementWidget.measureValue =
            measure.skeletal_muscle.formatToPercentageString()
        muscleMassMeasurementWidget.measureValue = measure.muscle_mass.formatToKgString()
        boneMassMeasurementWidget.measureValue = measure.bone_mass.formatToKgString()
        proteinMeasurementWidget.measureValue = measure.protein.formatToPercentageString()
        bmrMeasurementWidget.measureValue = measure.bmr.formatToKcalString()
        metabolicalAgeMeasurementsWidget.measureValue = measure.metabolical_age.formatToString()

        dateTextView.text = measure.date.toReadableDate()
        dateTextView.isEnabled = true
        dateTextView.isSelected = true
    }
}