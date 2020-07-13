package com.loguito.brainmove.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.loguito.brainmove.R
import com.loguito.brainmove.models.local.MeasurementOption
import com.loguito.brainmove.models.remote.Measure
import com.loguito.brainmove.viewmodels.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        intent?.let {
            if ("text/csv" == intent.type) {
                (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
                    val inpS = contentResolver.openInputStream(it)
                    inpS?.let { inputStream ->
                        handleReceivedFile(csvReader().readAll(inputStream)[1])
                        inputStream.close()
                    }
                }
            }
        }
    }

    private fun handleReceivedFile(data: List<String>) {
        val measureModel = Measure()
        for ((index, measure) in data.withIndex()) {
            if (index != 0) {
                val option = MeasurementOption.fromInt(index - 1)
                val value = measure.replace(Regex("[^\\d.]"), "").toDouble()
                when (option) {
                    MeasurementOption.WEIGHT -> measureModel.weight = value
                    MeasurementOption.BMI -> measureModel.bmi = value
                    MeasurementOption.BODY_FAT -> measureModel.body_fat = value
                    MeasurementOption.FAT_FREE_BODY -> measureModel.fat_free_body = value
                    MeasurementOption.SUBCUTANEOUS_FAT -> measureModel.subcutaneous_fat = value
                    MeasurementOption.VISCERAL_FAT -> measureModel.visceral_fat = value
                    MeasurementOption.BODY_WATER -> measureModel.body_water = value
                    MeasurementOption.SKELETAL_MUSCLE -> measureModel.skeletal_muscle = value
                    MeasurementOption.MUSCLE_MASS -> measureModel.muscle_mass = value
                    MeasurementOption.BONE_MASS -> measureModel.bone_mass = value
                    MeasurementOption.PROTEIN -> measureModel.protein = value
                    MeasurementOption.BMR -> measureModel.bmr = value
                    MeasurementOption.METABOLICAL_AGE -> measureModel.metabolical_age = value
                }
            } else {
                val format = SimpleDateFormat("MMM d, yyyy HH:mm:ss a", Locale.US)
                measureModel.date = format.parse(measure) ?: Date()
            }
        }
        sharedViewModel.setReceivedMeasure(measureModel)
    }
}
