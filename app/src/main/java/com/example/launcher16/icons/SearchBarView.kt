package com.example.launcher16.icons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.EditText

class SearchBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : EditText(context, attrs, defStyle) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#33FFFFFF")
    }
    private val dotsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF()

    private val googleColors = intArrayOf(
        Color.parseColor("#4285F4"),
        Color.parseColor("#EA4335"),
        Color.parseColor("#FBBC05"),
        Color.parseColor("#34A853")
    )

    init {
        setBackgroundColor(Color.TRANSPARENT)
        setTextColor(Color.WHITE)
        setHintTextColor(Color.parseColor("#B0FFFFFF"))
        hint = "Ara…"
        setPadding(dp(76), 0, dp(20), 0)
        isSingleLine = true
        textSize = 15f
        background = null
        setSelectAllOnFocus(false)
    }

    override fun onDraw(canvas: Canvas) {
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

        super.onDraw(canvas)
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()
}