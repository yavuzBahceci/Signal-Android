package com.app.image_detail

import androidx.fragment.app.Fragment
import com.app.navigation.router.ImageDetailRouter
import com.app.signal.domain.model.photo.Photo
import javax.inject.Inject

class ImageDetailRouterImpl @Inject constructor() : ImageDetailRouter {

    override fun getImageDetailFragment(photo: Photo): Fragment {
        TODO("Not yet implemented")
    }

}