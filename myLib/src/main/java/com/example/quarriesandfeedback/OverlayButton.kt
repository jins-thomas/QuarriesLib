package com.example.quarriesandfeedback

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.ImageView
import com.example.quarriesandfeedback.FeedBackAndQuarriesLib.mCurrentActivity

import java.io.ByteArrayOutputStream


class OverlayButton : Service() {

    private var mWindowManager: WindowManager? = null
    var mFloatingViewParams: WindowManager.LayoutParams? = null
    var transparentParams: WindowManager.LayoutParams? = null
    private lateinit var mFloatingView: View
    private var transparentView: View? = null
    private lateinit var closeButton: ImageView
    private lateinit var captureButton: ImageView
    var LAYOUT_FLAG = 0


    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null)
        // transparentView = LayoutInflater.from(this).inflate(R.layout.transparent_view, null)
        LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        closeButton = mFloatingView.findViewById<ImageView>(R.id.buttonClose)
        captureButton = mFloatingView.findViewById(R.id.captureButton)



        /**
        Floating View
         */
        /**
         * Floating View
         */
        mFloatingViewParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        mFloatingViewParams!!.x = 1000

        mWindowManager!!.addView(
            mFloatingView,
            mFloatingViewParams
        )


        mFloatingView.findViewById<View>(R.id.buttonClose)
            .setOnTouchListener(object : OnTouchListener {
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0f
                private var initialTouchY = 0f

                @SuppressLint("ResourceType")
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    val duration = event.eventTime - event.downTime
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialX = mFloatingViewParams!!.x
                            initialY = mFloatingViewParams!!.y
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            return true
                        }
                        MotionEvent.ACTION_UP -> {
                            if (duration < 300) {
                                if (mFloatingView.findViewById<View>(R.id.captureButton)
                                        .visibility == View.GONE
                                ) {
                                    mFloatingView.findViewById<View>(R.id.captureButton).visibility =
                                        View.VISIBLE
                                    closeButton.setImageResource(R.drawable.ic_baseline_close_24)
                                } else {
                                    mFloatingView.findViewById<View>(R.id.captureButton).visibility =
                                        View.GONE
                                    closeButton.setImageResource(R.drawable.ic_baseline_message_24)
                                }
                            }
                            return true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            mFloatingViewParams!!.x =
                                initialX + (event.rawX - initialTouchX).toInt()
                            mFloatingViewParams!!.y =
                                initialY + (event.rawY - initialTouchY).toInt()
                            mWindowManager!!.updateViewLayout(mFloatingView, mFloatingViewParams)
                            return true
                        }
                    }
                    return false
                }
            })

        captureButton.setOnClickListener{
            val rootView = mCurrentActivity?.window?.decorView
                ?.findViewById<View>(android.R.id.content)
            if (rootView != null) {
                screenShot(rootView)
            }
        }
    }

    fun screenShot(view: View): Bitmap? {
        mFloatingView.findViewById<View>(R.id.captureButton).visibility = View.GONE
        mFloatingView.findViewById<View>(R.id.buttonClose).visibility = View.GONE
        stopSelf()
        val bitmap = Bitmap.createBitmap(
            view.width,
            view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        val bStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 60, bStream)
        val byteArray = bStream.toByteArray()
        val intent = Intent(this, ImageEditor::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("BitmapImage", byteArray)
        startActivity(intent)
        return bitmap
    }


    }

