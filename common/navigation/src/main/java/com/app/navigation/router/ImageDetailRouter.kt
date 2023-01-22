package com.app.navigation.router

import androidx.fragment.app.Fragment
import com.app.signal.domain.model.photo.Photo

interface ImageDetailRouter {
    fun getImageDetailFragment(photo: Photo): Fragment
}