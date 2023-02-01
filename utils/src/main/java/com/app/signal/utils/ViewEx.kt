package com.app.signal.utils

import android.view.View
import androidx.core.view.isVisible

fun View.visible() {
    if (!isVisible) visibility = View.VISIBLE
}