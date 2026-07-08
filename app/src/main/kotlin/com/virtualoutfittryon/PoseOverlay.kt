package com.virtualoutfittryon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

class PoseOverlay(private val context: Context) {
    private val paint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 8f
        style = Paint.Style.STROKE
    }
    private val pointPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    fun draw(canvas: Canvas, result: PoseLandmarkerResult) {
        val landmarks = result.poseLandmarks().firstOrNull()?: return

        drawConnection(canvas, landmarks, 11, 13)
        drawConnection(canvas, landmarks, 13, 15)
        drawConnection(canvas, landmarks, 12, 14)
        drawConnection(canvas, landmarks, 14, 16)
        drawConnection(canvas, landmarks, 11, 12)
        drawConnection(canvas, landmarks, 11, 23)
        drawConnection(canvas, landmarks, 12, 24)
        drawConnection(canvas, landmarks, 23, 24)
        drawConnection(canvas, landmarks, 23, 25)
        drawConnection(canvas, landmarks, 25, 27)
        drawConnection(canvas, landmarks, 24, 26)
        drawConnection(canvas, landmarks, 26, 28)

        landmarks.forEach { landmark ->
            val x = landmark.x() * canvas.width
            val y = landmark.y() * canvas.height
            canvas.drawCircle(x, y, 10f, pointPaint)
        }
    }

    private fun drawConnection(canvas: Canvas, landmarks: List<NormalizedLandmark>, start: Int, end: Int) {
        if (start >= landmarks.size || end >= landmarks.size) return
        val startLandmark = landmarks[start]
        val endLandmark = landmarks[end]
        canvas.drawLine(
            startLandmark.x() * canvas.width, startLandmark.y() * canvas.height,
            endLandmark.x() * canvas.width, endLandmark.y() * canvas.height,
            paint
        )
    }
}
