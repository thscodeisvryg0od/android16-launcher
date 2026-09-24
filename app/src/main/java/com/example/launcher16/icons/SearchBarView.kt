package com.example.launcher16.icons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * Google Pixel search bar — matches reference screenshot:
 * [G] .................. [mic] [lens] [visual-search-circle]
 */
class SearchBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1F1F1F")
    }

    // Google "G" colors
    private val gBlue = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#4285F4"); isFakeBoldText = true; textAlign = Paint.Align.CENTER }
    private val gRed  = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#EA4335"); isFakeBoldText = true; textAlign = Paint.Align.CENTER }
    private val gYell = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#FBBC05"); isFakeBoldText = true; textAlign = Paint.Align.CENTER }
    private val gGreen= Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#34A853"); isFakeBoldText = true; textAlign = Paint.Align.CENTER }

    private val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 3.5f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val circleButtonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#3A3A3A")
    }

    private val rect = RectF()
    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val density = resources.displayMetrics.density
        val r = height / 2f

        rect.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, r, r, bgPaint)

        val cy = height / 2f
        val leftPad = 20f * density

        // Google G — drawn as 4 colored arcs around a small circle
        drawGoogleG(canvas, leftPad + 12f * density, cy, 14f * density)

        // Right side icons
        val rightEdge = width - 20f * density
        val iconSpacing = 40f * density

        // Visual search circle (rightmost) — circle with magnifier
        val vsCx = rightEdge - 18f * density
        canvas.drawCircle(vsCx, cy, 18f * density, circleButtonPaint)
        drawMagnifier(canvas, vsCx, cy, 8f * density)

        // Lens icon
        val lensCx = vsCx - iconSpacing - 10f * density
        drawLens(canvas, lensCx, cy, 10f * density)

        // Mic icon
        val micCx = lensCx - iconSpacing
        drawMic(canvas, micCx, cy, 10f * density)
    }

    private fun drawGoogleG(canvas: Canvas, cx: Float, cy: Float, r: Float) {
        // Simplified colored G using 4 arcs
        val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 5f * resources.displayMetrics.density
            strokeCap = Paint.Cap.BUTT
        }
        val rectF = RectF(cx - r, cy - r, cx + r, cy + r)

        arcPaint.color = Color.parseColor("#EA4335") // red top
        canvas.drawArc(rectF, -45f, -100f, false, arcPaint)
        arcPaint.color = Color.parseColor("#FBBC05") // yellow left
        canvas.drawArc(rectF, 145f, 60f, false, arcPaint)
        arcPaint.color = Color.parseColor("#34A853") // green bottom
        canvas.drawArc(rectF, 45f, 60f, false, arcPaint)
        arcPaint.color = Color.parseColor("#4285F4") // blue right
        canvas.drawArc(rectF, -10f, 60f, false, arcPaint)

        // Horizontal bar of G
        val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#4285F4")
            strokeWidth = 5f * resources.displayMetrics.density
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawLine(cx, cy, cx + r + 3f, cy, barPaint)
    }

    private fun drawMic(canvas: Canvas, cx: Float, cy: Float, size: Float) {
        // Mic body
        val bodyPaint = Paint(iconPaint).apply { style = Paint.Style.STROKE }
        val rectF = RectF(cx - size * 0.35f, cy - size, cx + size * 0.35f, cy + size * 0.15f)
        canvas.drawRoundRect(rectF, size * 0.4f, size * 0.4f, bodyPaint)
        // Mic stand
        path.reset()
        path.moveTo(cx - size * 0.6f, cy - size * 0.1f)
        path.quadTo(cx, cy + size * 0.6f, cx + size * 0.6f, cy - size * 0.1f)
        canvas.drawPath(path, bodyPaint)
    }

    private fun drawLens(canvas: Canvas, cx: Float, cy: Float, size: Float) {
        // Camera square with lens circle
        val bodyPaint = Paint(iconPaint).apply { style = Paint.Style.STROKE }
        val rectF = RectF(cx - size, cy - size * 0.75f, cx + size, cy + size * 0.75f)
        canvas.drawRoundRect(rectF, size * 0.25f, size * 0.25f, bodyPaint)
        canvas.drawCircle(cx, cy, size * 0.35f, bodyPaint)
    }

    private fun drawMagnifier(canvas: Canvas, cx: Float, cy: Float, size: Float) {
        val glassPaint = Paint(iconPaint).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f * resources.displayMetrics.density
        }
        canvas.drawCircle(cx - size * 0.2f, cy - size * 0.2f, size * 0.7f, glassPaint)
        canvas.drawLine(cx + size * 0.3f, cy + size * 0.3f,
            cx + size * 0.9f, cy + size * 0.9f, glassPaint)
    }
}