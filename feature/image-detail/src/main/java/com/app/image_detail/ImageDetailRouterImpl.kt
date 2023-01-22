package com.app.image_detail

import android.net.Uri
import androidx.fragment.app.Fragment
import com.app.image_detail.root.ImageDetailFragment
import com.app.navigation.router.ImageDetailRouter
import com.app.signal.domain.model.photo.Photo
import javax.inject.Inject

class ImageDetailRouterImpl @Inject constructor() : ImageDetailRouter {
    override fun getImageDetailFragment(title: String, uri: Uri): Fragment {
        return ImageDetailFragment.instantiate(title, uri)

    }

}