package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {

    // Paints
    private val textPaint = Paint().apply() {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
        textSize = context.dpToPx(16f)
    }

    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.colorPrimary)
    }

    private val progressPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
    }

    private val piePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
//        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.pie_color)
    }

    // Rectangles
    private val buttonRect = RectF()
    private val progressRect = RectF()
    private var buttonRadius = context.dpToPx(16f)
    private var offset = measuredWidth.toFloat()

    // Pie Progress Bar
    private val pieRect = RectF()
    private val pieRadius = 32f
    private var sweepAngle = 0f
//    private var pieTopOffset = 0f
//    private var pieBottomOffset = 0f
//    private var pieLeftOffset = 0f
//    private var pieRightOffset = 0f

    // Strings
    private var buttonText: String

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton)

        buttonText = typedArray.getString(R.styleable.LoadingButton_text) ?: "Download"
        typedArray.recycle()
    }

    // Colors
    private val buttonColor = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
    // Fonts
    private val fontSize = 55.0f
    // Rects
    private val r = Rect()

    private var valueAnimator: ValueAnimator? = null
    private var loading = false

    fun startLoading() {
        valueAnimator = ValueAnimator.ofFloat(1f, 0f).apply {
            addUpdateListener {
                offset = measuredWidth * it.animatedValue as Float
                sweepAngle = (1f - it.animatedValue as Float) * 360
                invalidate()
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    // TODO: call startProgressAnimation()
                    loading = false
                    Thread.sleep(2_000)
                    isClickable = true
                    invalidate()
                } })
            duration = 2000
        }

        loading = true
        isClickable = false
        valueAnimator?.start()
    }


//    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
//
//    }
//
//
//    init {
//
//    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paintBackground)
//
//        drawCenter(canvas!!, paintText1, buttonText)
//        drawCenter(canvas!!, paintText2, buttonText)

        buttonRadius = measuredHeight / 4f

        buttonRect.apply {
            top = 0f
            left = 0f
            right = measuredWidth.toFloat()
            bottom = measuredHeight.toFloat()
        }

        progressRect.apply {
            top = 0f
            left = 0f
            right = if (loading) measuredWidth.toFloat() - offset else 0f
            Log.d("onDraw", "Offset: " + offset.toString())
            Log.d("onDraw", "Right: " + right.toString())
            bottom = measuredHeight.toFloat()
        }

        pieRect.apply {
            top = measuredHeight.toFloat() / 2 - pieRadius
            bottom = measuredHeight.toFloat() / 2 + pieRadius
            left = measuredWidth.toFloat() * 3 / 4 - pieRadius
            right = measuredWidth.toFloat() * 3 / 4 + pieRadius
        }

        canvas.drawRoundRect(
            buttonRect,
            buttonRadius,
            buttonRadius,
            backgroundPaint)

        canvas.drawRoundRect(
            progressRect,
            buttonRadius,
            buttonRadius,
            progressPaint)

        drawCenter(canvas, textPaint, buttonText)
        if (loading) canvas.drawArc(pieRect, makePositiveAngle(-90f), sweepAngle, true, piePaint)
//        canvas.drawOval(pieRect, piePaint)

    }

    private fun drawCenter(canvas: Canvas, paint: Paint, text: String) {
        canvas.getClipBounds(r)
        val cHeight = r.height()
        val cWidth = r.width()
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds(text, 0, text.length, r)
        val x = cWidth / 2f - r.width() / 2f - r.left
        val y = cHeight / 2f + r.height() / 2f - r.bottom
        canvas.drawText(text, x, y, paint)
    }
    // Alternative way to drawCenter
    // canvas?.drawText(buttonText, widthSize.toFloat() / 2, heightSize.toFloat() / 2 - ((paintText2.descent() + paintText2.ascent()) / 2), paintText2)

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
//        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
//        val h: Int = resolveSizeAndState(
//            MeasureSpec.getSize(w),
//            heightMeasureSpec,
//            0
//        )
//        widthSize = w
//        heightSize = h
//        setMeasuredDimension(w, h)
//    }

}