package com.example.quarriesandfeedback

import android.app.Activity
import android.app.Application
import android.os.Bundle


object FeedBackAndQuarriesLib {
    const val SYSTEM_ALERT_WINDOW_PERMISSION: Int = 2020
    var mCurrentActivity: Activity? = null
    var callBackImageEditor: CallBackImageEditor? = null

    public fun setUpLib(application: Application)
    {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(
                activity: Activity,
                savedInstanceState: Bundle?
            ) {
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
                mCurrentActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {
                mCurrentActivity = null
            }

            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(
                activity: Activity,
                outState: Bundle
            ) {
            }

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    fun setListener(callBackImageEditor: CallBackImageEditor){
        this.callBackImageEditor=callBackImageEditor
    }
}