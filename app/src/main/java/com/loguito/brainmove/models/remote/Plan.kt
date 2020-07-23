package com.loguito.brainmove.models.remote

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Plan(
    val userId: String = "",
    val name: String = "",
    val goal: String = "",
    val toDate: Date = Date(),
    val fromDate: Date = Date(),
    val routines: List<Routine> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        parcel.createTypedArrayList(Routine) ?: emptyList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(name)
        parcel.writeString(goal)
        parcel.writeLong(toDate.time)
        parcel.writeLong(fromDate.time)
        parcel.writeTypedList(routines)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Plan> {
        override fun createFromParcel(parcel: Parcel): Plan {
            return Plan(parcel)
        }

        override fun newArray(size: Int): Array<Plan?> {
            return arrayOfNulls(size)
        }
    }
}