package com.example.quarriesandfeedback

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity(),CallBackImageEditor {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askPermission()

        FeedBackAndQuarriesLib.setListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, FeedBackAndQuarriesLib.SYSTEM_ALERT_WINDOW_PERMISSION)
        } else {
            startService(Intent(this, OverlayButton::class.java))
        }
    }

    override fun CallBackImageEditor(bitmap: Bitmap) {

    }
}
