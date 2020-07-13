package com.loguito.brainmove.models.remote

import android.os.Parcel
import android.os.Parcelable

data class Routine(
    val name: String = "",
    val routineNumber: Int = 0,
    val blocks: List<Block> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.createTypedArrayList(Block) ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(routineNumber)
        parcel.writeTypedList(blocks)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Routine> {
        override fun createFromParcel(parcel: Parcel): Routine {
            return Routine(parcel)
        }

        override fun newArray(size: Int): Array<Routine?> {
            return arrayOfNulls(size)
        }
    }

}