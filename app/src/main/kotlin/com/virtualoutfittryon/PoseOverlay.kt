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

    private val paint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 8f
        style = Paint.Style.STROKE
    }
    private val pointPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    fun updateResults(result: PoseLandmarkerResult) {
        results = result
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // This works for both.poseLandmarks and.poseLandmarks()
        val landmarksList: List<List<NormalizedLandmark>>? = try {
            results?.poseLandmarks() // try function first
        } catch (e: Exception) {
            results?.poseLandmarks // try property
        }

        if (landmarksList.isNullOrEmpty()) return
        val landmarks = landmarksList[0]

        val connections = listOf(
            11 to 13, 13 to 15, 12 to 14, 14 to 16,
            11 to 12, 11 to 23, 12 to 24, 23 to 24,
            23 to 25, 25 to 27, 24 to 26, 26 to 28
        )
        for((start, end) in connections) {
            if(start < landmarks.size && end < landmarks.size) {
                val s = landmarks[start]; val e = landmarks[end]
                canvas.drawLine(s.x() * width, s.y() * height, e.x() * width, e.y() * height, paint)
            }
        }
        for (landmark in landmarks) {
            canvas.drawCircle(landmark.x() * width, landmark.y() * height, 10f, pointPaint)
        }
    }
}
