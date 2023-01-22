package com.app.signal.control_kit.fragment.ex

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.app.signal.control_kit.ex.resignKeyboard

val Fragment.isKeyboardVisible: Boolean get() {
    val view = view ?: return false
    val insets = ViewCompat.getRootWindowInsets(view) ?: return false

    return insets.isVisible(WindowInsetsCompat.Type.ime())
}

fun Fragment.focusKeyboard(input: View) {
    val ctx = context ?: return

    input.requestFocus()

    val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(input.findFocus(), InputMethodManager.SHOW_IMPLICIT)
}

inline fun Fragment.resignKeyboard(crossinline finish: () -> Unit = {}) {
    val container = view
    val focused = requireActivity().currentFocus

    if (!isKeyboardVisible || focused == null || container == null) {
        finish()
        return
    }

    val view  = requireActivity().window.decorView

    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
        val isVisible = insets.isVisible(WindowInsetsCompat.Type.ime())

        if (!isVisible) {
            ViewCompat.setOnApplyWindowInsetsListener(v, null)
            finish()
        }

        insets
    }

    view.resignKeyboard()
}