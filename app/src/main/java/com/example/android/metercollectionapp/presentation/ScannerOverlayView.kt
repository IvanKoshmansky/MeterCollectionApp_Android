package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.example.android.metercollectionapp.R
import java.lang.Math.min

private const val WINDOW_SIZE = 0.75f

class ScannerOverlayView @JvmOverloads constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View (context, attrs, defStyleAttr) {

    private val borderPaint = Paint()
    private val overlayPaint = Paint()
    private val attrArray = context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.ScannerOverlayView,
        0,
        0
    )
    private var currentWidth = 0.0f
    private var currentHeight = 0.0f
    private var clippingRect = RectF()

    init {
        val overlayColor = attrArray.getColor(
            R.styleable.ScannerOverlayView_overlayColor,
            ContextCompat.getColor(context, android.R.color.background_dark)
        )
        overlayPaint.color = overlayColor
        overlayPaint.style = Paint.Style.FILL
        val borderColor = attrArray.getColor(
            R.styleable.ScannerOverlayView_borderColor,
            ContextCompat.getColor(context, android.R.color.background_dark)
        )
        val borderWidth = attrArray.getDimension(
            R.styleable.ScannerOverlayView_borderWidth,
            dpToPix(8)
        )
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth
        borderPaint.style = Paint.Style.STROKE
    }

    private fun dpToPix(dp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
        context.resources.displayMetrics)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // размеры в пискелях
        currentWidth = w.toFloat()
        currentHeight = h.toFloat()
        val rectWidth = currentWidth * WINDOW_SIZE
        val rectHeight = currentHeight * WINDOW_SIZE
        val squareSize = min(rectWidth, rectHeight)
        val centerX = w / 2
        val centerY = h / 2
        val halfSquareSize = squareSize / 2
        clippingRect = RectF(centerX - halfSquareSize, centerY - halfSquareSize, centerX + halfSquareSize,
            centerY + halfSquareSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            canvas.clipRect(clippingRect)
        } else {
            canvas.clipOutRect(clippingRect)
        }
        canvas.drawRect(0.0f, 0.0f, currentWidth, currentHeight, overlayPaint)
        canvas.restore()
        canvas.drawRect(clippingRect, borderPaint)
        invalidate()
    }
}
