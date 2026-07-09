package com.virtualoutfittryon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

class PoseOverlay(context: Context) : View(context) {
    private var results: PoseLandmarkerResult? = null

    private val paint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 8f
        style = Paint.Style.STROKE
    }

    fun updateResults(result: PoseLandmarkerResult) {
        results = result
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // TODO: Wire landmarks once we know your exact MediaPipe version
        // For now just draw a center dot so we know overlay works
        canvas.drawCircle(width / 2f, height / 2f, 20f, paint)
    }
}
