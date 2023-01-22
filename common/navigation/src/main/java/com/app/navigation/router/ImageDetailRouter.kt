package com.app.navigation.router

import android.net.Uri
import androidx.fragment.app.Fragment

interface ImageDetailRouter {
    fun getImageDetailFragment(title: String, uri: Uri): Fragment
}