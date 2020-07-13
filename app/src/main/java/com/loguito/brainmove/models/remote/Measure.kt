package com.loguito.brainmove.models.remote

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Measure(
    var weight: Double = 0.0,
    var bmi: Double = 0.0,
    var body_fat: Double = 0.0,
    var fat_free_body: Double = 0.0,
    var subcutaneous_fat: Double = 0.0,
    var visceral_fat: Double = 0.0,
    var body_water: Double = 0.0,
    var skeletal_muscle: Double = 0.0,
    var muscle_mass: Double = 0.0,
    var bone_mass: Double = 0.0,
    var protein: Double = 0.0,
    var bmr: Double = 0.0,
    var metabolical_age: Double = 0.0,
    var date: Date = Date(),
    var user_id: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readSerializable() as Date,
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(weight)
        parcel.writeDouble(bmi)
        parcel.writeDouble(body_fat)
        parcel.writeDouble(fat_free_body)
        parcel.writeDouble(subcutaneous_fat)
        parcel.writeDouble(visceral_fat)
        parcel.writeDouble(body_water)
        parcel.writeDouble(skeletal_muscle)
        parcel.writeDouble(muscle_mass)
        parcel.writeDouble(bone_mass)
        parcel.writeDouble(protein)
        parcel.writeDouble(bmr)
        parcel.writeDouble(metabolical_age)
        parcel.writeSerializable(date)
        parcel.writeString(user_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Measure> {
        override fun createFromParcel(parcel: Parcel): Measure {
            return Measure(parcel)
        }

        override fun newArray(size: Int): Array<Measure?> {
            return arrayOfNulls(size)
        }
    }
}