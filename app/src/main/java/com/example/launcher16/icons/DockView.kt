package com.example.launcher16.icons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import com.example.launcher16.AppInfo

class DockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#40FFFFFF")
    }
    private val rect = RectF()

    private var apps: List<AppInfo> = emptyList()
    private var onAppClick: java.util.function.Consumer<AppInfo>? = null
    private var pressedIndex = -1

    fun setApps(list: List<AppInfo>, listener: java.util.function.Consumer<AppInfo>) {
        apps = list
        onAppClick = listener
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val r = height * 0.35f
        rect.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, r, r, bgPaint)

        if (apps.isEmpty()) return
        val slotW = width.toFloat() / apps.size
        val baseSize = height * 0.55f
        val cy = height / 2f

        apps.forEachIndexed { i, app ->
            val cx = slotW * i + slotW / 2f
            val size = if (i == pressedIndex) baseSize * 0.85f else baseSize
            app.icon?.let { d: Drawable ->
                d.setBounds(
                    (cx - size / 2).toInt(),
                    (cy - size / 2).toInt(),
                    (cx + size / 2).toInt(),
                    (cy + size / 2).toInt()
                )
                d.draw(canvas)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (apps.isEmpty()) return false
        val slotW = width.toFloat() / apps.size
        val idx = (event.x / slotW).toInt().coerceIn(0, apps.size - 1)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                pressedIndex = idx; invalidate()
            }
            MotionEvent.ACTION_UP -> {
                if (idx == pressedIndex) onAppClick?.accept(apps[idx])
                pressedIndex = -1; invalidate()
                performClick()
            }
            MotionEvent.ACTION_CANCEL -> {
                pressedIndex = -1; invalidate()
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
