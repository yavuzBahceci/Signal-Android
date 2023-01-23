package com.app.signal.data.database

import com.app.signal.data.room.entities.PhotoEntity

class SignalDatabaseFake {
    val savedPhotos = mutableListOf<PhotoEntity>()
}