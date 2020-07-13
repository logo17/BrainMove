package com.loguito.brainmove.application

import android.app.Application
import com.cloudinary.Configuration
import com.cloudinary.android.MediaManager
import com.loguito.brainmove.R


class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MediaManager.init(
            this,
            Configuration.Builder().setCloudName(getString(R.string.cloudinary_api_name))
                .setApiKey(getString(R.string.cloudinary_api_key))
                .setApiSecret(getString(R.string.cloudinary_api_secret)).build()
        )
    }
}