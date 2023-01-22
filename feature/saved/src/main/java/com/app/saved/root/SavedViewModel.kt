package com.app.saved.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.saved.root.model.SavedItem
import com.app.signal.domain.model.State
import com.app.signal.domain.model.mapStateListItem
import com.app.signal.domain.service.PhotoService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
internal data class SavedViewModel @Inject constructor(
    private val photoService: PhotoService
) : ViewModel() {

    private val _actionFlow = MutableSharedFlow<SavedItem>(0, 1)

    fun deletePhoto(photoId: String): StateFlow<State<Unit>> {
        return photoService.deletePhoto(photoId)
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, State.Loading())
    }


    val savedPhotos = photoService.getSavedPhotos()
        .mapStateListItem {
            SavedItem.Photo(
                it.id,
                it.title,
                it.img,
                _actionFlow,
            )
        }.map { it.data }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun getActionFlow(): SharedFlow<SavedItem> {
        return _actionFlow
    }


}