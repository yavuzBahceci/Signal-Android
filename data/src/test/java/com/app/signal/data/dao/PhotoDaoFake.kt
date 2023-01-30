package com.app.signal.data.dao

import com.app.signal.data.database.SignalDatabaseFake
import com.app.signal.data.room.dao.photo.PhotoDao
import com.app.signal.data.room.entities.PhotoEntity

class PhotoDaoFake(private val appDatabaseFake: SignalDatabaseFake) : PhotoDao() {

    override fun getSavedPhotos(): List<PhotoEntity> {
        return appDatabaseFake.savedPhotos
    }

    override suspend fun deletePhoto(id: String): Int {
        val photoFound = appDatabaseFake.savedPhotos.filter { it.id == id }
        return if (photoFound.isNotEmpty()) {
            appDatabaseFake.savedPhotos.removeIf { it.id == id }
            1
        } else {
            -1
        }
    }

    override suspend fun insert(obj: PhotoEntity): Long {
        val result = appDatabaseFake.savedPhotos.add(obj)
        return if (result) {
            1
        } else -1
    }


    override suspend fun update(obj: PhotoEntity): Int {
        appDatabaseFake.savedPhotos.removeIf { it.id == obj.id }
        appDatabaseFake.savedPhotos.add(obj)
        return 1
    }
}