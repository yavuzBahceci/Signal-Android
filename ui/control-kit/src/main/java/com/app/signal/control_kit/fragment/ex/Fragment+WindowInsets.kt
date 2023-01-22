package com.app.signal.control_kit.fragment.ex

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.app.signal.control_kit.ex.current

fun getDefaultConsumableInsets(): Int {
    return WindowInsetsCompat.Type.systemBars() or
            WindowInsetsCompat.Type.ime() or
            WindowInsetsCompat.Type.systemGestures()
}

inline fun Fragment.consumeWindowInsets(
    types: Int = getDefaultConsumableInsets(),
    crossinline listener: (View, Insets, Boolean) -> WindowInsetsCompat
) {
    val view = view ?: return

    ViewCompat.setOnApplyWindowInsetsListener(view) { v, windowInsets ->
        val isTopFragment = parentFragmentManager.current == this
        val hasFocus = v.hasFocus()

        val keyboardIsActive = windowInsets.getInsets(WindowInsetsCompat.Type.ime()).bottom > 0

        val insets = if (isTopFragment && hasFocus) {
            windowInsets.getInsets(types)
        } else {
            windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.systemGestures()
            )
        }

        listener(v, insets, keyboardIsActive && isTopFragment && hasFocus)
    }
}

inline fun Fragment.consumeWindowInsets(
    types: Int = getDefaultConsumableInsets(),
    crossinline listener: (View, Insets) -> WindowInsetsCompat
) {
    consumeWindowInsets(types) { v, insets, _ ->
        listener(v, insets)
    }
}