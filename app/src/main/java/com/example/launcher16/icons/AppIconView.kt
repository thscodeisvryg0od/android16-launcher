package com.example.launcher16.icons

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator

class AppIconView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var icon: Drawable? = null
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val squircle = Path()
    private val rect = RectF()

    private var scale = 1f
    private val pressedScale = 0.85f
    private var bgColor = Color.parseColor("#33000000")
    private var cornerRadius = 0f

    fun setIcon(d: Drawable?) {
        icon = d
        invalidate()
    }

    fun setIconBackground(color: Int) {
        bgColor = color
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cornerRadius = w * 0.30f
        rect.set(0f, 0f, w.toFloat(), h.toFloat())
        squircle.reset()
        squircle.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f

        canvas.save()
        canvas.scale(scale, scale, cx, cy)

        bgPaint.color = bgColor
        canvas.drawPath(squircle, bgPaint)

        icon?.let { d ->
            val pad = (width * 0.12f).toInt()
            d.setBounds(pad, pad, width - pad, height - pad)
            d.draw(canvas)
        }

        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> animateScale(pressedScale)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> animateScale(1f)
        }
        return super.onTouchEvent(event)
    }

    private fun animateScale(target: Float) {
        ValueAnimator.ofFloat(scale, target).apply {
            duration = 150
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                scale = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }
}
