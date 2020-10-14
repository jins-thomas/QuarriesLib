package com.example.quarriesandfeedback
import android.graphics.Bitmap

 interface CallBackImageEditor {
     fun CallBackImageEditor(bitmap: Bitmap)
     fun OverlayButtonClickEvent(state:Boolean)
 }
