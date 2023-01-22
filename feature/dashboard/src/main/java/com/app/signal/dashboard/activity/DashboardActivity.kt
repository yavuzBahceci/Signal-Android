package com.app.signal.dashboard.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.signal.control_kit.IndicatorView
import com.app.signal.control_kit.fragment.RouterFragment
import com.app.signal.control_kit.fragment.ex.refreshSystemBarStyle
import com.app.signal.dashboard.R
import com.app.signal.dashboard.fragment.DashboardBundleKey
import com.app.signal.dashboard.fragment.DashboardFragment
import com.app.signal.dashboard.fragment.DashboardFragmentRequestKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val vm: DashboardViewModel by viewModels()

    private val appRouterFragment: RouterFragment
        get() {
            return supportFragmentManager.findFragmentById(R.id.app_router_fragment) as RouterFragment
        }

    private lateinit var indicator: IndicatorView

    fun openDashboardTab(@IdRes id: Int) {
        appRouterFragment.childFragmentManager.setFragmentResult(
            DashboardFragmentRequestKey,
            bundleOf(DashboardBundleKey.SelectedTabId to id)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        indicator = findViewById(R.id.indicator)


        val fragment: DashboardFragment?

        if (savedInstanceState == null) {
            fragment = DashboardFragment()

            appRouterFragment.push(fragment, false)

            appRouterFragment.executePending()
        }

        appRouterFragment.refreshSystemBarStyle()
    }


}