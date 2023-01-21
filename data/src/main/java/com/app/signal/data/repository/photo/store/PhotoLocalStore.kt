package com.app.signal.data.repository.photo.store

import androidx.room.withTransaction
import com.app.signal.data.room.AppDatabase
import com.app.signal.data.room.entities.PhotoEntity
import com.app.signal.domain.model.photo.Photo
import javax.inject.Inject

interface PhotoLocalStore {
    fun getSavedPhotos(): List<Photo>

    suspend fun savePhoto(dto: Photo)

    suspend fun removePhoto(id: String)
}

data class PhotoRoomStore @Inject constructor(
    private val db: AppDatabase
) : PhotoLocalStore {
    private val photoDao = db.photoDao

    override fun getSavedPhotos(): List<Photo> {
        return photoDao.getSavedPhotos()
    }

    override suspend fun savePhoto(dto: Photo) {
        db.withTransaction {
            val entity = PhotoEntity(
                dto.id,
                dto.img,
                dto.title
            )

            photoDao.insertOrUpdate(entity)
        }
    }

    override suspend fun removePhoto(id: String) {
        db.withTransaction {
            photoDao.deletePhoto(id)
        }
    }

}