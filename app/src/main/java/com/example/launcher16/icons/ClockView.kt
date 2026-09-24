package com.example.launcher16.icons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Pixel-style large clock widget.
 * Shows hour (big) over minute (big).
 */
class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val hourPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        isFakeBoldText = false
        textAlign = Paint.Align.CENTER
    }
    private val minutePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        isFakeBoldText = false
        textAlign = Paint.Align.CENTER
    }
    private val ampmPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#B0FFFFFF")
        textAlign = Paint.Align.CENTER
    }

    private var hours = ""
    private var minutes = ""
    private var ampm = ""

    private val handler = Handler(Looper.getMainLooper())
    private val ticker = object : Runnable {
        override fun run() {
            refresh()
            handler.postDelayed(this, 1000L)
        }
    }

    init {
        refresh()
        handler.post(ticker)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacks(ticker)
    }

    private fun refresh() {
        val now = Date()
        val h24 = SimpleDateFormat("HH", Locale.ENGLISH).format(now).toInt()
        val m = SimpleDateFormat("mm", Locale.ENGLISH).format(now)
        val h12 = if (h24 % 12 == 0) 12 else h24 % 12
        hours = String.format(Locale.ENGLISH, "%02d", h12)
        minutes = m
        ampm = if (h24 < 12) "AM" else "PM"
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val density = resources.displayMetrics.density
        val cx = width / 2f

        hourPaint.textSize = 96f * density
        minutePaint.textSize = 96f * density
        ampmPaint.textSize = 16f * density

        val totalH = hourPaint.textSize + minutePaint.textSize
        val startY = (height - totalH) / 2f + hourPaint.textSize

        canvas.drawText(hours, cx, startY, hourPaint)
        canvas.drawText(minutes, cx, startY + minutePaint.textSize * 0.95f, minutePaint)

        // AM/PM at bottom
        canvas.drawText(ampm, cx, height - 8f * density, ampmPaint)
    }
}
