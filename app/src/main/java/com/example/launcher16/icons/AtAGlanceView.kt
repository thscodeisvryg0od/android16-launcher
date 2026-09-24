package com.example.launcher16.icons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Pixel-style At a Glance widget.
 * Left: date. Right: weather in circular icon.
 */
class AtAGlanceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#CC1F1F1F")
    }
    private val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        isFakeBoldText = true
    }
    private val weatherCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#33FFFFFF")
    }
    private val weatherPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        isFakeBoldText = true
        textAlign = Paint.Align.CENTER
    }
    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#B0FFFFFF")
    }
    private val rect = RectF()

    private var dateText = ""
    private var dayText = ""

    init {
        refresh()
    }

    fun refresh() {
        val now = Date()
        dayText = SimpleDateFormat("EEE", Locale.ENGLISH).format(now)
        dateText = SimpleDateFormat("MMM d", Locale.ENGLISH).format(now)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val density = resources.displayMetrics.density

        val r = 28f * density
        rect.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, r, r, bgPaint)

        val padX = 22f * density
        val cy = height / 2f

        // Left: Date in two sizes
        datePaint.textSize = 18f * density
        datePaint.color = Color.parseColor("#B0FFFFFF")
        canvas.drawText(dayText, padX, cy - 2f * density, datePaint)

        datePaint.textSize = 22f * density
        datePaint.color = Color.WHITE
        canvas.drawText(dateText, padX, cy + 22f * density, datePaint)

        // Right: Weather circle
        val circleR = 24f * density
        val circleCx = width - circleR - 20f * density
        canvas.drawCircle(circleCx, cy, circleR, weatherCirclePaint)

        weatherPaint.textSize = 20f * density
        canvas.drawText("22°", circleCx, cy + 7f * density, weatherPaint)

        // Three dots
        val dotsX = width - circleR * 2 - 40f * density
        for (i in 0..2) {
            canvas.drawCircle(dotsX, cy - 4f * density + i * 6f * density,
                1.5f * density, dotPaint)
        }
    }
}