package com.app.signal.discover.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.signal.discover.root.model.AnyPhoto
import com.app.signal.discover.root.model.DiscoverAction
import com.app.signal.discover.root.model.DiscoverItem
import com.app.signal.discover.root.model.SearchItem
import com.app.signal.discover.utils.cursorFlow
import com.app.signal.domain.form.photo.SearchQueryParams
import com.app.signal.domain.model.State
import com.app.signal.domain.model.mapStateListItem
import com.app.signal.domain.service.PhotoService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
internal data class DiscoverViewModel @Inject constructor(
    private val photoService: PhotoService
) : ViewModel() {
    private val _searchStateFlow = MutableStateFlow("")
    private val _actionFlow = MutableSharedFlow<DiscoverAction>(0, 1)
    private val _loadMoreFlow = MutableSharedFlow<Long>(replay = 1)

    val itemsFlow: Flow<State<List<DiscoverItem>>?>

    init {
        val loadMore = _loadMoreFlow.map { }

        itemsFlow = _searchStateFlow.debounce(1500)
            .distinctUntilChanged()
            .flatMapLatest { searchText ->
                if (searchText.isNotEmpty()) {
                    cursorFlow(
                        trigger = loadMore,
                        request = {
                            photoService.searchPhotos(
                                SearchQueryParams(
                                    searchText = searchText,
                                    page = it
                                )
                            )
                        }
                    )
                } else {
                    flowOf()
                }
            }.mapStateListItem {
                DiscoverItem.Photo(
                    it.id,
                    it.title,
                    it.img,
                    _actionFlow,
                )
            }.flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, State.Empty())
    }

    val recentSearches = photoService.observePreviousSearches()
        .map { textList ->
            textList.map {
                SearchItem(it, _actionFlow)
            }.filter { it.text.isNotEmpty() }
        }.flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun triggerSearch(text: String) {
        _searchStateFlow.value = text
    }

    fun triggerToLoadMore() {
        viewModelScope.launch {
            _loadMoreFlow.emit(System.currentTimeMillis())
        }
    }

    fun getActionFlow(): SharedFlow<DiscoverAction> {
        return _actionFlow
    }

    suspend fun savePhoto(photo: DiscoverItem.Photo): StateFlow<State<Unit>> {
        return photoService.savePhoto(AnyPhoto(photo.id, photo.image!!, photo.title))
            .flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.Lazily, State.Loading())
    }
}