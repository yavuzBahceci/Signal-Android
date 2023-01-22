package com.app.saved

import androidx.fragment.app.Fragment
import com.app.navigation.router.SavedRouter
import com.app.saved.root.SavedFragment
import javax.inject.Inject

class SavedRouterImpl @Inject constructor() : SavedRouter {

    override fun getSavedFragment(): Fragment {
        return SavedFragment()
    }
}