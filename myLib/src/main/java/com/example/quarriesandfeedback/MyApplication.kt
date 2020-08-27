package com.example.quarriesandfeedback

import android.app.Application

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FeedBackAndQuarriesLib.setUpLib(this)
    }
}