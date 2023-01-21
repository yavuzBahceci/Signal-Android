package com.app.signal.domain.service

import com.app.signal.domain.form.photo.SearchQueryParams
import com.app.signal.domain.model.photo.Photo
import com.app.signal.domain.repository.PhotosPageState
import com.app.signal.domain.repository.PhotosState
import com.app.signal.domain.repository.UnitState
import kotlinx.coroutines.flow.Flow

interface PhotoService {
    fun searchPhotos(searchQueryParams: SearchQueryParams): Flow<PhotosPageState>

    fun getSavedPhotos(): Flow<PhotosState>

    fun savePhoto(photo: Photo): Flow<UnitState>

    fun deletePhoto(id: String): Flow<UnitState>
}