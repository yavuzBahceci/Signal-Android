package com.app.signal.control_kit

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import com.app.signal.control_kit.ex.resolveColor
import com.app.signal.control_kit.ex.resolveFont
import com.app.signal.design_system.R.*
import com.app.signal.design_system.TextAppearance
import com.app.signal.design_system.ThemeColor
import com.app.signal.design_system.Weight

class ProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var startTitle: CharSequence? = null
        set(value) {
            field = value
            invalidate()
        }

    var endTitle: CharSequence? = null
        set(value) {
            field = value
            invalidate()
        }

    var progress: Float = 0.0f
        set(value) {
            field = value
            invalidate()
        }

    private val _backgroundPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = context.resolveColor(ThemeColor.Content.Opacity10)
    }

    private val _filledPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = context.resolveColor(ThemeColor.Solid.Green)
    }

    private val _textPaint = TextPaint().apply {
        val font = context.resolveFont(TextAppearance.Body2(Weight.SemiBold))

        typeface = font.typeface
        textSize = font.scaledSize

        color = context.resolveColor(ThemeColor.Content.Opacity50)
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textDirection = TEXT_DIRECTION_LOCALE
    }

    private val _textFrame = Rect()

    private val _strokeHeight = resources.getDimension(
        dimen.stroke_medium
    )

    private val animationDuration = resources
        .getInteger(android.R.integer.config_longAnimTime)
        .toLong()

    init {
        layoutDirection = LAYOUT_DIRECTION_LOCALE
    }

    fun setStartTitle(@StringRes resId: Int) {
        startTitle = resources.getString(resId)
    }

    fun setProgress(progress: Float, animated: Boolean) {
        if (!animated) {
            this.progress = progress
            return
        }

        val animator = ValueAnimator.ofFloat(this.progress, progress)
        animator.duration = animationDuration

        animator.addUpdateListener {
            this.progress = it.animatedValue as Float
        }

        animator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var height = _strokeHeight

        val buffer = startTitle ?: endTitle

        buffer?.toString()?.let {
            val spacing = resources.getDimension(
                dimen.spacing_sm
            )

            _textPaint.getTextBounds(it, 0, it.length, _textFrame)

            height += spacing + _textFrame.height() * 2
        }

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height.toInt())
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val startText: CharSequence?
        val endText: CharSequence?

        if (layoutDirection == LAYOUT_DIRECTION_RTL) {
            startText = endTitle
            endText = startTitle
        } else {
            startText = startTitle
            endText = endTitle
        }

        startText?.toString()?.also {
            _textPaint.getTextBounds(it, 0, it.length, _textFrame)

            val x = _textFrame.width() / 2f
            val y = height - _textFrame.height() / 2f

            canvas.drawText(it, x, y, _textPaint)
        }

        endText?.toString()?.also {
            _textPaint.getTextBounds(it, 0, it.length, _textFrame)

            val x = width - _textFrame.width() / 2f
            val y = height - _textFrame.height() / 2f

            canvas.drawText(it, x, y, _textPaint)
        }

        if (layoutDirection == LAYOUT_DIRECTION_RTL) {
            canvas.scale(-1f, 1f, width / 2f, height / 2f)
        }

        val w = width.toFloat()
        val h = _strokeHeight
        val radii = h / 2

        canvas.drawRoundRect(0f, 0f, w, h, radii, radii,  _backgroundPaint)

        if (progress == 0f) {
            return
        }

        var filledWidth = (w * progress) / 100

        if (filledWidth < radii) {
            filledWidth = radii * 2
        }

        if (filledWidth > w) {
            filledWidth = w
        }

        canvas.drawRoundRect(0f, 0f, filledWidth, h, radii, radii,  _filledPaint)
    }
}