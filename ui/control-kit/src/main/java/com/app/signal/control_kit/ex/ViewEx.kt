package com.app.signal.control_kit.ex

import android.animation.Animator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Px
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.app.signal.design_system.ColorProvider

fun View.slideOut() {
    slideUsing(height.toFloat() * 2, 0f) {
        isVisible = false
    }
}

fun View.slideIn() {
    isVisible = true
    slideUsing(0f, 1f) {}
}

fun View.slideUsing(translationY: Float, alpha: Float = 1f, complete: () -> Unit = {}) {
    val duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

    animate()
        .alpha(alpha)
        .translationY(translationY)
        .setInterpolator(DecelerateInterpolator())
        .setDuration(duration)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) { }

            override fun onAnimationEnd(animation: Animator) {
                complete()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        .start()
}

fun View.fadeOut() {
    fade(0f) {
        isVisible = false
    }
}

fun View.fadeIn() {
    isVisible = true
    fade(1f) {}
}

fun View.fade(alpha: Float = 1f, complete: () -> Unit = {}) {
    val duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

    animate()
        .alpha(alpha)
        .setInterpolator(DecelerateInterpolator())
        .setDuration(duration)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) { }

            override fun onAnimationEnd(animation: Animator) {
                complete()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        .start()
}

fun View.updateMargins(
    @Px
    left: Int? = null,
    @Px
    top: Int? = null,
    @Px
    right: Int? = null,
    @Px
    bottom: Int? = null
) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        setMargins(
            left ?: leftMargin,
            top ?: topMargin,
            right ?: rightMargin,
            bottom ?: bottomMargin
        )
    }
}


fun ConstraintLayout.updateHorizontalBias(views: List<View>, bias: Float) {
    ConstraintSet().also {
        it.clone(this)

        views.forEach { view ->
            it.setHorizontalBias(view.id, bias)
        }

        it.applyTo(this)
    }
}

fun ConstraintLayout.updateVerticalBias(views: List<View>, bias: Float) {
    ConstraintSet().also {
        it.clone(this)

        views.forEach { view ->
            it.setVerticalBias(view.id, bias)
        }

        it.applyTo(this)
    }
}


inline fun ConstraintLayout.updateConstraints(
    view: View,
    crossinline applier: (cs: ConstraintSet, view: View) -> Unit
) {
    ConstraintSet().also {
        it.clone(this)
        applier(it, view)
        it.applyTo(this)
    }

    requestLayout()
}

fun View.setBackgroundColor(color: ColorProvider) {
    setBackgroundColor(context.resolveColor(color))
}

fun View.resignKeyboard() {
    val ctx = context
    val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    imm.hideSoftInputFromWindow(windowToken, 0)
    clearFocus()
}