package com.app.signal.control_kit.fragment.ex

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.promptToast(@StringRes strId: Int) {
    promptToast(resources.getString(strId))
}

fun Fragment.promptToast(throwable: Throwable?) {
    promptToast(throwable?.localizedMessage ?: "")
}

fun Fragment.promptToast(msg: String) {
    if (msg.isBlank()) {
        return
    }

    val toast = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT)
    toast.show()
}