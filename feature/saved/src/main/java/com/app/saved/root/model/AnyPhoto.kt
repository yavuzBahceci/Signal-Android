package com.app.saved.root.model

import com.app.signal.domain.model.photo.Image
import com.app.signal.domain.model.photo.Photo

data class AnyPhoto(override val id: String, override val img: Image, override val title: String) : Photo