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
 * Pixel-style At a Glance: date left, weather circle right.
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
        textAlign = Paint.Align.LEFT
    }
    private val weatherCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#33FFFFFF")
    }
    private val weatherPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        isFakeBoldText = true
        textAlign = Paint.Align.CENTER
    }
    private val sunPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FBBC05")
        style = Paint.Style.FILL
    }
    private val rect = RectF()

    private var fullDate = ""

    init {
        refresh()
    }

    fun refresh() {
        val now = Date()
        fullDate = SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(now)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val density = resources.displayMetrics.density

        val r = 24f * density
        rect.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, r, r, bgPaint)

        val padX = 20f * density
        val cy = height / 2f

        // Date
        datePaint.textSize = 16f * density
        canvas.drawText(fullDate, padX, cy + 6f * density, datePaint)

        // Weather circle (right)
        val circleR = 22f * density
        val circleCx = width - circleR - 16f * density
        canvas.drawCircle(circleCx, cy, circleR, weatherCirclePaint)

        // Sun icon
        canvas.drawCircle(circleCx - 6f * density, cy - 3f * density, 5f * density, sunPaint)
        // Cloud (simple arc)
        weatherCirclePaint.color = Color.WHITE
        canvas.drawCircle(circleCx + 2f * density, cy + 1f * density, 5f * density, weatherCirclePaint)

        // Temperature
        weatherPaint.textSize = 12f * density
        weatherPaint.color = Color.WHITE
        canvas.drawText("22°", circleCx, cy + 18f * density, weatherPaint)
    }
}