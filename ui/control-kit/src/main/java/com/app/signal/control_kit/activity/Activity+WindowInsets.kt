package com.app.signal.control_kit.activity

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun observeWindowInsets(root: View, listener: (View, Insets) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(root) { v, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

        listener(v, insets)

        if (!v.isInLayout) {
            v.requestLayout()
        }

        windowInsets
    }
}