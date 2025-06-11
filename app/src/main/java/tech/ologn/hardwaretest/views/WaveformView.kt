package tech.ologn.hardwaretest.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class WaveformView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.RED
        strokeWidth = 4f
        isAntiAlias = true
    }

    private val maxPoints = 100
    private val amplitudes = mutableListOf<Int>()

    fun updateAmplitude(amp: Int) {
        // Keep max `maxPoints` points in the list
        if (amplitudes.size >= maxPoints) {
            amplitudes.removeAt(0)
        }
        amplitudes.add(amp)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerY = height / 2f
        val pointWidth = width / maxPoints.toFloat()

        for (i in amplitudes.indices) {
            val ampNorm = amplitudes[i] / 32767f
            val barHeight = ampNorm * height
            val x = i * pointWidth
            canvas.drawLine(x, centerY - barHeight / 2, x, centerY + barHeight / 2, paint)
        }
    }

    fun reset() {
        amplitudes.clear()
        invalidate()
    }
}