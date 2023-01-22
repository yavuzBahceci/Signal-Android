package com.app.signal.dashboard.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.app.signal.control_kit.ex.switchToChild
import com.app.signal.control_kit.ex.updateMargins
import com.app.signal.control_kit.fragment.AnyFragment
import com.app.signal.control_kit.fragment.ScrollableFragment
import com.app.signal.control_kit.fragment.ex.consumeWindowInsets
import com.app.signal.control_kit.fragment.ex.getDefaultConsumableInsets
import com.app.signal.control_kit.fragment.ex.requireRouterFragmentManager
import com.app.signal.dashboard.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import loadAttrDimension

const val DashboardFragmentRequestKey = "DASHBOARD_REQ_KEY_SELECT_TAB"

object DashboardBundleKey {
    const val SelectedTabId = "DASHBOARD_BUNDLE_KEY_SELECTED_TAB_ID"
}


@AndroidEntryPoint
class DashboardFragment : AnyFragment(R.layout.fragment_dashboard) {

    private lateinit var container: ViewGroup
    private lateinit var bottomNav: BottomNavigationView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        container = view.findViewById(R.id.container)
        bottomNav = view.findViewById(R.id.bottom_nav)

        bottomNav.also {
            it.setOnApplyWindowInsetsListener(null)

            it.setOnItemSelectedListener { item ->
                switchToFragment(item.itemId)
                true
            }
        }

        if (childFragmentManager.primaryNavigationFragment == null) {
            bottomNav.selectedItemId = R.id.discover
        }

        val offset = view.context.loadAttrDimension(
            com.app.signal.design_system.R.dimen.spacing_md
        )

        consumeWindowInsets { _, insets ->
            bottomNav.updateMargins(
                bottom = insets.bottom + offset
            )

            if (bottomNav.measuredHeight == 0) {
                bottomNav.measure(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            val adjustedInsets = Insets.add(
                insets,
                Insets.of(
                    0,
                    0,
                    0,
                    bottomNav.measuredHeight + offset * 3
                )
            )

            val newInsets = WindowInsetsCompat
                .Builder()
                .setInsets(
                    getDefaultConsumableInsets(),
                    adjustedInsets
                )
                .build()

            childFragmentManager.fragments.forEach { fragment ->
                val fragmentView = fragment.view ?: return@forEach
                ViewCompat.dispatchApplyWindowInsets(fragmentView, newInsets)
            }

            WindowInsetsCompat.CONSUMED
        }

        bindFragmentResultListeners()
    }

    private fun bindFragmentResultListeners() {
        val fm = requireRouterFragmentManager()

        fm.setFragmentResultListener(DashboardFragmentRequestKey, viewLifecycleOwner) { _, bundle ->
            val selectedTabId = bundle.getInt(DashboardBundleKey.SelectedTabId, R.id.discover)
            bottomNav.selectedItemId = selectedTabId
        }
    }



    private fun switchToFragment(@IdRes id: Int) {
        if (id == bottomNav.selectedItemId) {
            val fragment = childFragmentManager.primaryNavigationFragment as? ScrollableFragment
            fragment?.resetScroll()
        }

        childFragmentManager.switchToChild(R.id.container, "DASHBOARD_TAG_$id") {
            createFragmentFrom(id)
        }
    }

    private fun createFragmentFrom(@IdRes id: Int): Fragment {
        /*R.id.discover -> // DiscoverFragment()
        else -> // SavedFragment()*/
        TODO("")
    }
}