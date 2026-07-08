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

        // Draw connections
        drawConnection(canvas, landmarks, 11, 13) // left shoulder to elbow
        drawConnection(canvas, landmarks, 13, 15) // left elbow to wrist
        drawConnection(canvas, landmarks, 12, 14) // right shoulder to elbow
        drawConnection(canvas, landmarks, 14, 16) // right elbow to wrist
        drawConnection(canvas, landmarks, 11, 12) // shoulders
        drawConnection(canvas, landmarks, 11, 23) // left torso
        drawConnection(canvas, landmarks, 12, 24) // right torso
        drawConnection(canvas, landmarks, 23, 24) // hips
        drawConnection(canvas, landmarks, 23, 25) // left hip to knee
        drawConnection(canvas, landmarks, 25, 27) // left knee to ankle
        drawConnection(canvas, landmarks, 24, 26) // right hip to knee
        drawConnection(canvas, landmarks, 26, 28) // right knee to ankle

        // Draw points
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
