package com.virtualoutfittryon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class OverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.parseColor("#88FF4081")  // Semi-transparent pink
        style = Paint.Style.FILL
    }

    // Simple rectangle representing a t-shirt for demo
    var shirtRect = android.graphics.RectF(0f, 0f, 0f, 0f)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw sample shirt
        canvas.drawRect(shirtRect, paint)
        
        // TODO: Later draw proper clothing bitmap warped to body landmarks
    }
}
