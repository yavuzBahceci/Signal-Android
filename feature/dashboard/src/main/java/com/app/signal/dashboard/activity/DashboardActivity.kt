package com.app.signal.dashboard.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.app.signal.control_kit.fragment.RouterFragment
import com.app.signal.control_kit.fragment.ex.refreshSystemBarStyle
import com.app.signal.dashboard.R
import com.app.signal.dashboard.fragment.DashboardFragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val appRouterFragment: RouterFragment
        get() {
            return supportFragmentManager.findFragmentById(R.id.app_router_fragment) as RouterFragment
        }

    private lateinit var indicator: CircularProgressIndicator

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