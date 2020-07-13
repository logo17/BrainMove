package com.loguito.brainmove.ext

import com.github.mikephil.charting.formatter.ValueFormatter
import com.loguito.brainmove.R
import com.loguito.brainmove.formatters.SimpleFormatter
import com.loguito.brainmove.formatters.WeightFormatter
import com.loguito.brainmove.models.local.MeasurementOption

fun MeasurementOption.toTitle(): Int = when (this) {
    MeasurementOption.WEIGHT -> R.string.weight_label_text
    MeasurementOption.BMI -> R.string.bmi_label_text
    MeasurementOption.BODY_FAT -> R.string.body_fat_label_text
    MeasurementOption.FAT_FREE_BODY -> R.string.fat_free_body_weight_label_text
    MeasurementOption.SUBCUTANEOUS_FAT -> R.string.subcutaneous_fat_label_text
    MeasurementOption.VISCERAL_FAT -> R.string.visceral_fat_label_text
    MeasurementOption.BODY_WATER -> R.string.body_water_label_text
    MeasurementOption.SKELETAL_MUSCLE -> R.string.skeletal_muscle_label_text
    MeasurementOption.MUSCLE_MASS -> R.string.muscle_mass_label_text
    MeasurementOption.BONE_MASS -> R.string.bone_mass_label_text
    MeasurementOption.PROTEIN -> R.string.protein_label_text
    MeasurementOption.BMR -> R.string.bmr_label_text
    MeasurementOption.METABOLICAL_AGE -> R.string.metabolic_age_label_text
}

fun MeasurementOption.toValueFormatter(): ValueFormatter? = when (this) {
    MeasurementOption.WEIGHT -> WeightFormatter()
    MeasurementOption.FAT_FREE_BODY -> WeightFormatter()
    MeasurementOption.MUSCLE_MASS -> WeightFormatter()
    MeasurementOption.BONE_MASS -> WeightFormatter()
    else -> SimpleFormatter()
}