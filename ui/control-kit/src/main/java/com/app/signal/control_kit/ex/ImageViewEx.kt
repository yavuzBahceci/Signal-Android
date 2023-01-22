package com.app.signal.control_kit.ex

import android.net.Uri
import android.widget.ImageView
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.ViewSizeResolver
import com.app.signal.utils.model.Img

enum class FillMode {
    CenterInside, AspectFill, Center, AspectFit
}

fun ImageView.loadImage(uri: Uri?, mode: FillMode = FillMode.AspectFit) {
    scaleType = when (mode) {
        FillMode.CenterInside -> ImageView.ScaleType.CENTER_INSIDE
        FillMode.AspectFill -> ImageView.ScaleType.CENTER_CROP
        FillMode.AspectFit -> ImageView.ScaleType.FIT_CENTER
        FillMode.Center -> ImageView.ScaleType.CENTER
    }

    val request = ImageRequest.Builder(context)
        .data(uri)
        .size(ViewSizeResolver(this, false))
        .allowHardware(true)
        .crossfade(true)
        .target(this)
        .build()

    context.imageLoader.enqueue(request)
}

fun ImageView.loadImg(img: Img?) = when (img) {
    is Img.Link -> loadImage(img.uri, FillMode.AspectFill)

    is Img.WithContext -> loadImage(img.lambda(context), FillMode.AspectFill)

    is Img.Resource -> {
        scaleType = ImageView.ScaleType.CENTER_INSIDE
        setImageResource(img.resourceId)
    }

    else -> setImageDrawable(null)
}