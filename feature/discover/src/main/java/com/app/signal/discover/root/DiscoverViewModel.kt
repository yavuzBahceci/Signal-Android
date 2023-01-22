package com.app.signal.discover.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.signal.discover.root.model.DiscoverAction
import com.app.signal.discover.root.model.DiscoverItem
import com.app.signal.discover.root.model.SearchItem
import com.app.signal.discover.utils.cursorFlow
import com.app.signal.domain.form.photo.SearchQueryParams
import com.app.signal.domain.model.mapStateListItem
import com.app.signal.domain.service.PhotoService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
internal data class DiscoverViewModel @Inject constructor(
    private val photoService: PhotoService
) : ViewModel() {
    private val _searchStateFlow = MutableStateFlow("")
    private val _actionFlow = MutableSharedFlow<DiscoverAction>(0, 1)
    private val _loadMoreFlow = MutableSharedFlow<Instant>(replay = 1)

    val itemsFlow: Flow<List<DiscoverItem>?>

    init {
        val loadMore = _loadMoreFlow.map { }

        itemsFlow = _searchStateFlow.debounce(2000)
            .distinctUntilChanged()
            .flatMapLatest { searchText ->
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
            }.mapStateListItem {
                DiscoverItem.Photo(
                    it.id,
                    it.title,
                    it.img,
                    _actionFlow,
                )
            }.map { it.data }
    }


    val recentSearches = photoService.observePreviousSearches()
        .map { textList ->
            textList.map {
                SearchItem(it, _actionFlow)
            }.filter { it.text.isNotEmpty() }
        }.flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun triggerSearch(text: String) {
        _searchStateFlow.value = text
    }

    fun triggerToLoadMore() {
        viewModelScope.launch {
            _loadMoreFlow.emit(Instant.now())
        }
    }

    fun getActionFlow(): SharedFlow<DiscoverAction> {
        return _actionFlow
    }
}