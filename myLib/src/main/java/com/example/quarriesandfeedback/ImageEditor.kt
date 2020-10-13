package com.example.quarriesandfeedback

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.FileInputStream


class ImageEditor : AppCompatActivity() {

    lateinit var done:Button
    lateinit var undo:Button
    lateinit var imageView:ImageView;
    lateinit var byteArray: ByteArray
    private lateinit var bmp:Bitmap
    lateinit var view: DrawView
    lateinit var callBackImageEditor: CallBackImageEditor
    lateinit var yellowLayout: TextView
    lateinit var blackLayout: TextView
    lateinit var filename:String


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_editor)

            filename= intent.getStringExtra("BitmapImage").toString()
            try {
                val `is`: FileInputStream = openFileInput(filename)
                bmp = BitmapFactory.decodeStream(`is`)
                `is`.close()
            } catch (e: Exception) {
                e.printStackTrace()

        }

        done=findViewById(R.id.doneButton)
        undo=findViewById(R.id.undo)
        imageView=findViewById(R.id.imageView)
        view=findViewById(R.id.drawView)
        yellowLayout=findViewById(R.id.yellowLayout)
        blackLayout=findViewById(R.id.blackLayout)
        callBackImageEditor = FeedBackAndQuarriesLib.callBackImageEditor!!
        imageView.setImageBitmap(bmp)


        yellowLayout.setOnClickListener{
             var color = Color.parseColor("#FEFA0B")
            view.visibility=View.VISIBLE;
            view.setBrushColor(color)
        }
        blackLayout.setOnClickListener{
            view.visibility=View.VISIBLE;
            view.setBrushColor(Color.BLACK)
        }

        done.setOnClickListener{
            startService(Intent(this, OverlayButton::class.java))
            val bitmap = Bitmap.createBitmap(
                view.width,
                view.height, Bitmap.Config.ARGB_8888
            )
            view.draw(Canvas(bitmap))

            callBackImageEditor.CallBackImageEditor(bitmap)
            finish()
        }

        undo.setOnClickListener{
            view.undo()
        }
    }

    override fun onBackPressed() {
        startService(Intent(this, OverlayButton::class.java))
        finish()
    }
}