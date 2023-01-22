package com.app.dashboard.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.signal.domain.form.photo.SearchQueryParams
import com.app.signal.domain.model.State
import com.app.signal.domain.model.photo.Photo
import com.app.signal.domain.repository.PhotosState
import com.app.signal.domain.service.AppStorage
import com.app.signal.domain.service.PhotoService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val photoService: PhotoService,
    private val appStorage: AppStorage
) : ViewModel() {

    val recentSearches = appStorage.observePreviousSearches()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val photos = photoService
        .searchPhotos(SearchQueryParams("Snake", 7))
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, State.Loading())

    fun deletePhoto(photoId: String): StateFlow<State<Unit>> {
        return photoService.deletePhoto(photoId)
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, State.Loading())
    }

    fun savePhoto(photo: Photo): StateFlow<State<Unit>> {
        return photoService.savePhoto(photo)
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, State.Loading())
    }

    fun getSavedPhotos(): Flow<PhotosState> {
        return photoService.getSavedPhotos()
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, State.Loading())
    }
}