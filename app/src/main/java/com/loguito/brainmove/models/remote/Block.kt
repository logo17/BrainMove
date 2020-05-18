package com.loguito.brainmove.models.remote

import android.os.Parcel
import android.os.Parcelable

data class Block(
    val description: String = "",
    val unit: String = "",
    val duration: Int = 0,
    val imageUrl: String = "",
    val name: String = "",
    val exercises: List<Exercise> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(Exercise) ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(description)
        parcel.writeInt(duration)
        parcel.writeString(imageUrl)
        parcel.writeString(name)
        parcel.writeTypedList(exercises)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Block> {
        override fun createFromParcel(parcel: Parcel): Block {
            return Block(parcel)
        }

        override fun newArray(size: Int): Array<Block?> {
            return arrayOfNulls(size)
        }
    }

}