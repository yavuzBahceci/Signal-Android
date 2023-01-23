package com.app.signal.data.repository.store

import com.app.signal.data.dao.PhotoDaoFake
import com.app.signal.data.dto.response.photo.ImageDto
import com.app.signal.data.repository.photo.store.PhotoLocalStore
import com.app.signal.data.room.entities.PhotoEntity
import com.app.signal.domain.model.photo.Photo

class PhotoRoomFake(private val photoDaoFake: PhotoDaoFake) : PhotoLocalStore {
    override fun getSavedPhotos(): List<Photo> {
        return photoDaoFake.getSavedPhotos()
    }

    override suspend fun savePhoto(dto: Photo) {
        photoDaoFake.insert(
            PhotoEntity(
                dto.id,
                ImageDto(dto.img.smallImageUrl, dto.img.largeImageUrl, dto.img.thumbNailUrl),
                dto.title
            )
        )
    }

    override suspend fun removePhoto(id: String) {
        photoDaoFake.deletePhoto(id)
    }
}