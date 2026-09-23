package com.example.launcher16.icons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * Google Pixel-style pill search bar.
 * Left: colored "G" logo. Right: mic + lens icons.
 */
class SearchBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#40FFFFFF")
    }
    private val gPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#4285F4")
        textSize = 42f
        isFakeBoldText = true
    }
    private val hintPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#B0FFFFFF")
        textSize = 34f
    }
    private val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 5f
        strokeCap = Paint.Cap.ROUND
    }
    private val rect = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val r = height / 2f
        val pad = 4f
        rect.set(pad, pad, width - pad, height - pad)
        canvas.drawRoundRect(rect, r, r, bgPaint)

        val cy = height / 2f
        val padX = 20f * resources.displayMetrics.density

        // Google "G"
        canvas.drawText("G", padX, cy + gPaint.textSize / 3f, gPaint)

        // Hint
        canvas.drawText("Search…", padX + 50f * resources.displayMetrics.density,
            cy + hintPaint.textSize / 3f, hintPaint)

        // Mic icon (simple)
        val micX = width - 100f * resources.displayMetrics.density
        canvas.drawLine(micX, cy - 12f, micX, cy + 6f, iconPaint)
        canvas.drawLine(micX - 8f, cy + 6f, micX + 8f, cy + 6f, iconPaint)

        // Lens icon (simple circle + dot)
        val lensX = width - 50f * resources.displayMetrics.density
        canvas.drawCircle(lensX, cy, 10f, iconPaint)
        canvas.drawLine(lensX + 7f, cy + 7f, lensX + 14f, cy + 14f, iconPaint)
    }
}
