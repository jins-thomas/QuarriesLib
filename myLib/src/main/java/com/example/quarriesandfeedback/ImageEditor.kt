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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_editor)
        if(intent.getByteArrayExtra("BitmapImage")!=null){
            byteArray = intent.getByteArrayExtra("BitmapImage")!!
            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
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
                imageView.width,
                imageView.height, Bitmap.Config.ARGB_8888
            )
            view.draw(Canvas(bitmap))
            imageView.draw(Canvas(bitmap))
            callBackImageEditor.CallBackImageEditor(bitmap)
            finish()
        }

        undo.setOnClickListener{
            view.undo()
        }
    }
}