package com.loguito.brainmove.utils

import android.content.Context
import java.io.IOException
import java.io.InputStream

object Utils {
    fun getJsonFromAssets(context: Context, fileName: String): String? {
        val jsonString: String
        jsonString = try {
            val inputStream: InputStream = context.getAssets().open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return jsonString
    }
}