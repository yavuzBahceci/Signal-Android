package com.app.signal.discover

import androidx.fragment.app.Fragment
import com.app.navigation.router.DiscoverRouter
import com.app.signal.discover.root.DiscoverFragment
import javax.inject.Inject


class DiscoverRouterImpl @Inject constructor() : DiscoverRouter {
    override fun getDiscoverFragment(): Fragment {
        return DiscoverFragment()
    }
}