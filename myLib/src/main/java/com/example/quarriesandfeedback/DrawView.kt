package com.example.quarriesandfeedback

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import java.util.*


class DrawView : androidx.appcompat.widget.AppCompatImageView {
    private var color = Color.parseColor("#FFFFFF33")
    private var width = 60f
    private val holderList: MutableList<Holder> = ArrayList()

    private inner class Holder internal constructor(color: Int, width: Float) {
        var path: Path
        var paint: Paint

        init {
            path = Path()
            paint = Paint()
            paint.strokeWidth = width
            paint.color = color
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeCap = Paint.Cap.ROUND
            paint.isAntiAlias = true;
            paint.isDither = true;
            paint.alpha=120
        }
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        holderList.add(Holder(color, width))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (holder in holderList.distinctBy { it.path }) {
            canvas.drawPath(holder.path, holder.paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventX = event.x
        val eventY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                holderList.add(Holder(color, width))
                holderList[holderList.size - 1].path.moveTo(eventX, eventY)
                return true
            }
            MotionEvent.ACTION_MOVE -> holderList[holderList.size - 1].path.lineTo(
                eventX,
                eventY
            )
            MotionEvent.ACTION_UP -> {
            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun resetPaths() {
        for (holder in holderList) {
            holder.path.reset()
        }
        invalidate()
    }

    fun setBrushColor(color: Int) {
        this.color = color
    }

    fun undo() {
        if (holderList.size > 0) {
            holderList.removeAt(holderList.size - 1)
            invalidate()
        }
    }

    fun setWidth(width: Float) {
        this.width = width
    }
}