package com.app.signal.control_kit.fragment.ex

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.signal.control_kit.ex.pop
import com.app.signal.control_kit.fragment.RouterFragment


fun Fragment.findNearestRouter(): RouterFragment? {
    var fragment: Fragment? = this

    while (fragment != null) {
        if (fragment is RouterFragment) {
            return fragment
        }
        fragment = fragment.parentFragment
    }

    return fragment
}

fun Fragment.findRouterFragmentManager(): FragmentManager? {
    return findNearestRouter()?.childFragmentManager
}

fun Fragment.requireRouterFragmentManager(): FragmentManager {
    return findRouterFragmentManager() as FragmentManager
}

inline fun Fragment.registerOnBackPressedCallback(crossinline isEnabled: (FragmentManager) -> Boolean) {
    val activity = activity ?: return

    val callback = object : OnBackPressedCallback(isEnabled(childFragmentManager)) {
        override fun handleOnBackPressed() {
            childFragmentManager.pop()
        }
    }
    activity.onBackPressedDispatcher.addCallback(this, callback)

    childFragmentManager.addOnBackStackChangedListener {
        callback.isEnabled = isEnabled(childFragmentManager)
    }
}