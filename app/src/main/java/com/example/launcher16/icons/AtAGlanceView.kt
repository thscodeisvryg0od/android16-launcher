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

class AtAGlanceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#33FFFFFF")
    }
    private val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 42f
        isFakeBoldText = true
    }
    private val weatherPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#B0FFFFFF")
        textSize = 32f
    }
    private val rect = RectF()

    private var dateText: String = ""
    private var dayText: String = ""

    init {
        updateDate()
    }

    private fun updateDate() {
        val now = Date()
        dateText = SimpleDateFormat("EEEE", Locale.ENGLISH).format(now)
        dayText = SimpleDateFormat("MMM d", Locale.ENGLISH).format(now)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val r = 24f * resources.displayMetrics.density
        rect.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, r, r, bgPaint)

        val padX = 24f * resources.displayMetrics.density
        val padY = 20f * resources.displayMetrics.density

        canvas.drawText("$dateText, $dayText", padX, height / 2f + 8f, datePaint)
        canvas.drawText("☀  22°", width - 140f * resources.displayMetrics.density,
            height / 2f + 8f, weatherPaint)
    }
}
