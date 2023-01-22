package com.app.signal.data.service.photo

import com.app.signal.domain.form.photo.SearchQueryParams
import com.app.signal.domain.model.photo.Photo
import com.app.signal.domain.repository.PhotoRepository
import com.app.signal.domain.repository.PhotosPageState
import com.app.signal.domain.repository.PhotosState
import com.app.signal.domain.repository.UnitState
import com.app.signal.domain.service.AppStorage
import com.app.signal.domain.service.PhotoService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class PhotoServiceImpl @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val storage: AppStorage
) : PhotoService {

    override fun observePreviousSearches(): Flow<List<String>> {
        return storage.observePreviousSearches()
    }

    override fun searchPhotos(searchQueryParams: SearchQueryParams): Flow<PhotosPageState> {
        saveSearch(searchQueryParams.searchText)
        return photoRepository.getSearchResults(searchQueryParams)
    }

    override fun getSavedPhotos(): Flow<PhotosState> {
        return photoRepository.getSavedPhotos()
    }

    override fun savePhoto(photo: Photo): Flow<UnitState> {
        return photoRepository.savePhoto(photo)
    }

    override fun deletePhoto(id: String): Flow<UnitState> {
        return photoRepository.removeSavedPhoto(id)
    }

    private fun saveSearch(searchText: String) {
        val currentList = storage.previousSearches.toMutableList()
        if (currentList.contains(searchText)) {
            currentList.remove(searchText)
        } else if (currentList.size >= 10) {
            currentList.removeLast()
        }
        currentList.add(0, searchText)
        storage.previousSearches = currentList
    }
}