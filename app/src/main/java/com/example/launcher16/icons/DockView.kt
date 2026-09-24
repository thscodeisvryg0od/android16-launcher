package com.example.launcher16.icons

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import com.example.launcher16.AppInfo

class DockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var apps: List<AppInfo> = emptyList()
    private var onAppClick: java.util.function.Consumer<AppInfo>? = null
    private var onAppLongClick: java.util.function.Consumer<AppInfo>? = null
    private var pressedIndex = -1

    private val handler = Handler(Looper.getMainLooper())
    private var longPressed = false
    private var downIndex = -1

    private val longPressRunnable = Runnable {
        if (downIndex >= 0 && downIndex < apps.size) {
            longPressed = true
            onAppLongClick?.accept(apps[downIndex])
            pressedIndex = -1
            invalidate()
        }
    }

    fun setApps(list: List<AppInfo>,
                click: java.util.function.Consumer<AppInfo>,
                longClick: java.util.function.Consumer<AppInfo>? = null) {
        apps = list
        onAppClick = click
        onAppLongClick = longClick
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (apps.isEmpty()) return

        val slotW = width.toFloat() / apps.size
        val baseSize = height * 0.75f
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
                pressedIndex = idx
                downIndex = idx
                longPressed = false
                invalidate()
                handler.postDelayed(longPressRunnable, 550L)
            }
            MotionEvent.ACTION_UP -> {
                handler.removeCallbacks(longPressRunnable)
                if (idx == pressedIndex && !longPressed) {
                    onAppClick?.accept(apps[idx])
                }
                pressedIndex = -1
                downIndex = -1
                invalidate()
                performClick()
            }
            MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacks(longPressRunnable)
                pressedIndex = -1
                downIndex = -1
                invalidate()
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}