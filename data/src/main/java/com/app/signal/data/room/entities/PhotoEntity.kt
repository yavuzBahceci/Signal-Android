package com.app.signal.data.room.entities

import androidx.room.Entity
import com.app.signal.domain.model.photo.Image
import com.app.signal.domain.model.photo.Photo

@Entity(tableName = "photo", primaryKeys = ["id"])
data class PhotoEntity(
    override val id: String,
    val image: Image,
    override val title: String
) : Photo {
    override val img: Image
        get() = image
}
