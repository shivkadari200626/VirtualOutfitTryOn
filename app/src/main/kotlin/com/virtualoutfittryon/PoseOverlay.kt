package com.virtualoutfittryon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

class PoseOverlay(context: Context) : View(context) {
    private var results: PoseLandmarkerResult? = null

    private val linePaint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 8f
        style = Paint.Style.STROKE
    }
    private val pointPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        strokeWidth = 10f
    }

    fun updateResults(result: PoseLandmarkerResult) {
        results = result
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val result = results?: return
        
        // Try both APIs to be safe
        val landmarksList = try { result.poseLandmarks() } catch (e: Exception) { null }
        if (landmarksList.isNullOrEmpty()) return
        
        val landmarks = landmarksList[0]
        if (landmarks.size < 33) return

        // Draw connections
        val connections = listOf(
            11 to 13, 13 to 15, 12 to 14, 14 to 16, // arms
            11 to 12, 11 to 23, 12 to 24, 23 to 24, // torso
            23 to 25, 25 to 27, 24 to 26, 26 to 28, // legs
            27 to 29, 28 to 30, 29 to 31, 30 to 32 // feet
        )
        for((start, end) in connections) {
            if(start < landmarks.size && end < landmarks.size) {
                val s = landmarks[start]; val e = landmarks[end]
                canvas.drawLine(s.x() * width, s.y() * height, e.x() * width, e.y() * height, linePaint)
            }
        }
        // Draw points
        for (landmark in landmarks) {
            canvas.drawCircle(landmark.x() * width, landmark.y() * height, 8f, pointPaint)
        }
    }
}
