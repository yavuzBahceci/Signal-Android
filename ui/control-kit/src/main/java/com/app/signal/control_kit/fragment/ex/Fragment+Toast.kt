package com.app.signal.control_kit.fragment.ex

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.promptToast(msg: String) {
    if (msg.isBlank()) {
        return
    }

    val toast = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT)
    toast.show()
}