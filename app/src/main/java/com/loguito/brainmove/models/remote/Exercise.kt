package com.loguito.brainmove.models.remote

import android.os.Parcel
import android.os.Parcelable

data class Exercise(
    val backgroundImageUrl: String = "",
    val demoUrl: String = "",
    val name: String = "",
    val quantity: String = "",
    val explanations: List<String> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(backgroundImageUrl)
        parcel.writeString(demoUrl)
        parcel.writeString(name)
        parcel.writeString(quantity)
        parcel.writeStringList(explanations)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Exercise

        if (backgroundImageUrl != other.backgroundImageUrl) return false
        if (demoUrl != other.demoUrl) return false
        if (name != other.name) return false
        if (quantity != other.quantity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = backgroundImageUrl.hashCode()
        result = 31 * result + demoUrl.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + quantity.hashCode()
        return result
    }

    companion object CREATOR : Parcelable.Creator<Exercise> {
        override fun createFromParcel(parcel: Parcel): Exercise {
            return Exercise(parcel)
        }

        override fun newArray(size: Int): Array<Exercise?> {
            return arrayOfNulls(size)
        }
    }


}