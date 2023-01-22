package com.app.signal.control_kit.fragment.sheet

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.app.signal.control_kit.R
import com.app.signal.control_kit.ex.pop
import com.app.signal.control_kit.fragment.ex.SystemBarStyle
import com.app.signal.control_kit.fragment.ex.SystemBarStyleProvider
import com.app.signal.control_kit.fragment.ex.refreshSystemBarStyle
import com.google.android.material.bottomsheet.BottomSheetBehavior

abstract class SheetFragment(@LayoutRes layoutId: Int) : Fragment(layoutId),
    SystemBarStyleProvider {
    protected var inDismiss: Boolean = false

    override var systemBarStyle: SystemBarStyle = SystemBarStyle.Dark
        set(value) {
            field = value
            refreshSystemBarStyle()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            elevation = resources.getDimension(R.dimen.elevation_fragment)
            outlineProvider = null
            isFocusable = false
            isFocusableInTouchMode = false

            setOnClickListener {
                dismiss()
            }
        }

        val layout = view as SheetCoordinatorLayout

        layout.behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                systemBarStyle = if (newState != BottomSheetBehavior.STATE_EXPANDED || layout.fitToContent) {
                    SystemBarStyle.Dark
                }  else {
                    SystemBarStyle.Light
                }

                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    open fun dismiss() {
        if (inDismiss) {
            return
        }

        inDismiss = true

        parentFragmentManager.pop()
    }
}