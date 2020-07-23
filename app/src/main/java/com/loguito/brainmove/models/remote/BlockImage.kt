package com.loguito.brainmove.models.remote

data class BlockImage(var imageUrl: String = "", var name: String = "") {
    override fun toString() = name
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlockImage

        if (imageUrl != other.imageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        return imageUrl.hashCode()
    }
}