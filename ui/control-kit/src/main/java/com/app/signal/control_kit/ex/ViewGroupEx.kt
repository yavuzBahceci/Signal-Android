package com.app.signal.control_kit.ex

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlin.math.roundToInt
import kotlin.reflect.KClass
import kotlin.reflect.cast

fun ViewGroup.findViewUnderTouch(ev: MotionEvent): View {
    val x = ev.x.roundToInt()
    val y = ev.y.roundToInt()

    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (x > child.left && x < child.right && y > child.top && y < child.bottom) {
            return child
        }
    }
    return this
}

fun<T: View> View.findViewByType(clazz: KClass<T>): T? {
    if (clazz.isInstance(this)) {
        return clazz.cast(this)
    }

    if (this !is ViewGroup) {
        return null
    }

    for (i in 0 until childCount) {
        val child = getChildAt(i).findViewByType(clazz)

        if (child != null) {
            return  child
        }
    }

    return null
}
