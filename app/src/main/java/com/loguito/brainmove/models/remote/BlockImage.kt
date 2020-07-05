package com.loguito.brainmove.models.remote

data class BlockImage(var imageUrl: String = "", var name: String = "") {
    override fun toString() = name
}