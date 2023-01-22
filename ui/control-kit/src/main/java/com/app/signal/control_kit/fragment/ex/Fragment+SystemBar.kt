package com.app.signal.control_kit.fragment.ex

import android.os.Build
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenStateAtLeast
import com.app.signal.control_kit.ex.resolveColor
import com.app.signal.design_system.ThemeColor
import isUsingNightMode
import kotlinx.coroutines.launch

enum class SystemBarStyle {
    Dark, Light, TransparentDark, Transparent;
}

interface SystemBarStyleProvider {
    val systemBarStyle: SystemBarStyle
}

fun Fragment.refreshSystemBarStyle() {
    lifecycleScope.launch {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            viewLifecycleOwner.lifecycle.whenStateAtLeast(Lifecycle.State.STARTED) {
                applyStatusBarStyle()
            }
        } else {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                applyStatusBarStyle()
            }
        }
    }
}


private fun Fragment.applyStatusBarStyle() {
    val ctx = requireContext()

    val style = when {
        this is SystemBarStyleProvider -> systemBarStyle
        ctx.isUsingNightMode -> SystemBarStyle.Dark
        else -> SystemBarStyle.Light
    }

    val view = requireView()
    val window = requireActivity().window
    val controller = WindowInsetsControllerCompat(window, view)

    val navigationBarColor: Int
    val navigationBarDividerColor: Int
    var isDark: Boolean = ctx.isUsingNightMode

    when (style) {
        SystemBarStyle.TransparentDark -> {
            navigationBarColor = android.R.color.transparent
            navigationBarDividerColor = android.R.color.transparent
            isDark = true
        }

        SystemBarStyle.Transparent -> {
            navigationBarColor = android.R.color.transparent
            navigationBarDividerColor = android.R.color.transparent
            isDark = ctx.isUsingNightMode
        }

        else -> {
            val rootInsets = ViewCompat.getRootWindowInsets(view)

            val hasGestureBasedMenu = if (rootInsets != null) {
                val systemGestures = rootInsets.getInsets(WindowInsetsCompat.Type.systemGestures())
                systemGestures.left > 0 || systemGestures.right > 0
            } else {
                false
            }

            if (hasGestureBasedMenu) {
                val color = ctx.resolveColor(ThemeColor.Solid.Transparent)

                navigationBarColor = color
                navigationBarDividerColor = color
            } else {
                navigationBarColor = ctx.resolveColor(ThemeColor.SystemMenu.Background)
                navigationBarDividerColor = ctx.resolveColor(ThemeColor.SystemMenu.Border)
            }
        }
    }

    // FALLBACK: API 23 doesn't support grey navigation bars
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        val fallback = ctx.resolveColor(ThemeColor.SystemMenu.Fallback)
        window.navigationBarColor = fallback

        // FALLBACK: API 21 doesn't support grey navigation and status bars
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.statusBarColor = fallback
        }
    } else {
        window.navigationBarColor = navigationBarColor
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        window.navigationBarDividerColor = navigationBarDividerColor
    }

    controller.isAppearanceLightStatusBars = !isDark
    controller.isAppearanceLightNavigationBars = !isDark
}