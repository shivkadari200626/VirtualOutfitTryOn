package com.virtualoutfittryon

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class OverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint()
    var currentClothing: Bitmap? = null
    var clothingRect = RectF()

    fun setClothing(bitmap: Bitmap?) {
        currentClothing = bitmap
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        currentClothing?.let { bitmap ->
            canvas.drawBitmap(bitmap, null, clothingRect, paint)
        }
    }
}
