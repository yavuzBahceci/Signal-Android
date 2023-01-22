package com.app.signal.control_kit

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnEnd
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import com.app.signal.control_kit.ex.resolveColor
import com.app.signal.design_system.ThemeColor


class IndicatorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val animationDuration = resources
        .getInteger(android.R.integer.config_shortAnimTime)
        .toLong()

    private lateinit var logoView: LogoView

    var isActive: Boolean = false
        set(value) {
            field = value

            val alpha: Float

            if (value) {
                isVisible = true
                alpha = 1f
            } else {
                alpha = 0f
            }

            animate()
                .alpha(alpha)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(animationDuration)
                .setListener(object : AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        if (value) {
                            showLogo()
                        }
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        isVisible = value

                        if (!value) {
                            hideLogo()
                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}
                })
                .start()

        }

    var hasBackground: Boolean = true
        set(value) {
            field = value

            if (value) {
                val bgColor = context.resolveColor(ThemeColor.Background.Primary)
                setBackgroundColor(ColorUtils.setAlphaComponent(bgColor, 180))
            } else {
                background = null
            }
        }

    init {
        setup()
        applyAttributes(attrs)
    }

    private fun setup() {
        isClickable = true
        isFocusable = true
        isVisible = false
        alpha = 0f
        outlineProvider = null
        elevation = resources.getDimension(R.dimen.elevation_loading_indicator)

        logoView = LogoView(context).also {
            it.id = View.generateViewId()
            addView(it, 0, 0)
        }

        val set = ConstraintSet()
        set.clone(this)

        val sides = listOf(
            ConstraintSet.TOP,
            ConstraintSet.START,
            ConstraintSet.BOTTOM,
            ConstraintSet.END
        )

        sides.forEach {
            set.connect(
                logoView.id,
                it,
                ConstraintSet.PARENT_ID,
                it
            )
        }

        set.constrainPercentWidth(logoView.id, 0.1f)
        set.setDimensionRatio(logoView.id, "1:1")

        set.applyTo(this)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        context.theme
            .obtainStyledAttributes(attrs, R.styleable.IndicatorView, 0, 0)
            .also {
                isActive = it.getBoolean(R.styleable.IndicatorView_isActive, false)
                hasBackground = it.getBoolean(R.styleable.IndicatorView_hasBackground, true)

                it.recycle()
            }
    }

    private fun showLogo() {
        logoView.startAnimation()
    }

    private fun hideLogo() {
        logoView.pauseAnimation()
    }
}

private class LogoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private enum class SliceDirection {
        TopStart, TopEnd, BottomEnd
    }

    private val animationDuration = resources
        .getInteger(android.R.integer.config_shortAnimTime)
        .toLong()

    private val _animator = AnimatorSet()

    private val _rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = ResourcesCompat.getColor(
            resources,
            com.app.signal.design_system.R.color.solid_green,
            null
        )
        it.style = Paint.Style.FILL
    }

    private var _rectDim: Float = 0f

    private var _focusedRectStartDelta: Float = 1f
    private var _focusedRectTopDelta: Float = 1f

    init {
        val first = createAnimator( 1f) {
            val value = it.animatedValue as Float

            _focusedRectTopDelta = 2f
            _focusedRectStartDelta = 1f - value

            postInvalidateOnAnimation()
        }

        first.interpolator = AccelerateDecelerateInterpolator()

        val second = createAnimator( 2f) {
            val value = it.animatedValue as Float

            _focusedRectTopDelta = 2f - value
            _focusedRectStartDelta = 0f

            postInvalidateOnAnimation()
        }

        val third = createAnimator( 1f) {
            val value = it.animatedValue as Float

            _focusedRectTopDelta = 0f
            _focusedRectStartDelta = value

            postInvalidateOnAnimation()
        }

        third.startDelay = 200

        val last = createAnimator( 2f) {
            val value = it.animatedValue as Float

            _focusedRectTopDelta = value
            _focusedRectStartDelta = 1f

            postInvalidateOnAnimation()
        }

        last.interpolator = AccelerateDecelerateInterpolator()

        _animator.doOnEnd {
            it.start()
        }

        _animator.playSequentially(
            first,
            second,
            third,
            last
        )
    }

    private fun createAnimator(targetValue: Float, listener: (ValueAnimator) -> Unit): ValueAnimator {
        val animator = ValueAnimator.ofFloat(0f, targetValue)

        animator.duration = animationDuration

        animator.addUpdateListener(listener)

        return animator
    }

    private fun createRectPath(position: Point): Path {
        val x = position.x.toFloat()
        val y = position.y.toFloat()
        val dim = _rectDim

        val path = Path()

        path.addRect(x, y, x + dim, y + dim, Path.Direction.CW)

        path.close()

        return path
    }

    private fun createTrianglePath(position: Point, direction: SliceDirection): Path {
        val x = position.x.toFloat()
        val y = position.y.toFloat()
        val dim = _rectDim

        val path = Path()

        when (direction) {
            SliceDirection.TopStart -> {
                path.moveTo(x + dim, y)

                path.lineTo(x + dim, y)
                path.lineTo(x + dim, y + dim)
                path.lineTo(x, y + dim)
            }

            SliceDirection.TopEnd -> {
                path.moveTo(x, y)

                path.lineTo(x, y + dim)
                path.lineTo(x + dim, y + dim)
                path.lineTo(x, y)
            }

            SliceDirection.BottomEnd -> {
                path.moveTo(x, y)

                path.lineTo(x + dim, y)
                path.lineTo(x, y + dim)
                path.lineTo(x, y)
            }
        }

        path.close()

        return path
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val dim = _rectDim.toInt()

        for (col in 0 until COLS) {
            for (row in 0 until ROWS) {
                var x = row * dim
                var y = col * dim

                if (col == 0 && row == 0) {
                    x = (dim * _focusedRectStartDelta).toInt()
                    y = (dim * _focusedRectTopDelta).toInt()
                }

                val point = Point(x, y)

                val path = when {
                    col == 0 && row > 0 -> null
                    col == 1 && row == 0 -> createTrianglePath(point, SliceDirection.TopStart)
                    col == 1 && row == ROWS - 1 -> createTrianglePath(point, SliceDirection.TopEnd)
                    col == COLS - 1 && row == 0 -> null
                    col == COLS - 1 && row == ROWS - 1 -> createTrianglePath(point, SliceDirection.BottomEnd)
                    col == COLS / 2 && row == ROWS / 2 -> null
                    else -> createRectPath(point)
                }

                path?.let {
                    canvas.drawPath(path, _rectPaint)
                }
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        _rectDim = (width / COLS).toFloat()
    }

    fun startAnimation() {
        clearAnimation()

        _animator.start()
    }

    fun pauseAnimation() {
        _animator.pause()
    }

    companion object {
        const val ROWS = 3
        const val COLS = 4
    }
}