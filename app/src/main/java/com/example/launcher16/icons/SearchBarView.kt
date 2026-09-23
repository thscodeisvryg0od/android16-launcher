package com.example.launcher16.icons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class SearchBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#33FFFFFF")
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        isAntiAlias = true
    }
    private val dotsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF()

    private val googleColors = intArrayOf(
        Color.parseColor("#4285F4"),
        Color.parseColor("#EA4335"),
        Color.parseColor("#FBBC05"),
        Color.parseColor("#34A853")
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val r = height / 2f

        rect.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, r, r, bgPaint)

        val dotRadius = height * 0.06f
        val startX = height * 0.55f
        val cy = height / 2f
        for (i in 0..3) {
            dotsPaint.color = googleColors[i]
            canvas.drawCircle(startX + i * dotRadius * 2.4f, cy, dotRadius, dotsPaint)
        }

        textPaint.textSize = height * 0.30f
        val textY = cy - (textPaint.descent() + textPaint.ascent()) / 2f
        canvas.drawText("Ara…", startX + dotRadius * 11f, textY, textPaint)
    }
}
