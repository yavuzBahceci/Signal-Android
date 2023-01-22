package com.app.signal.data.repository.photo.store

import com.app.signal.data.dto.response.generic.PhotoResponse
import com.app.signal.data.dto.response.photo.PhotoDto
import com.app.signal.data.dto.response.photo.PhotoListDto
import com.app.signal.data.rest.api.PhotoApi
import com.app.signal.domain.form.photo.SearchQueryParams
import com.app.signal.domain.model.PhotoListPage
import javax.inject.Inject

interface PhotoRemoteStore {

    suspend fun getPhotos(searchQueryParams: SearchQueryParams): PhotoListPage<PhotoDto>
}

data class PhotoRetrofitStore @Inject constructor(
    private val photoRestApi: PhotoApi
) : PhotoRemoteStore {

    override suspend fun getPhotos(searchQueryParams: SearchQueryParams): PhotoListPage<PhotoDto> {
        val response =
            photoRestApi.searchPhotos(searchQueryParams.searchText, searchQueryParams.page)

        return createPhotoListPage(response)
    }

    private fun createPhotoListPage(response: PhotoResponse<PhotoListDto>) =
        if (response.photos.page < response.photos.pages) {
            PhotoListPage(response.photos.photo, response.photos.page + 1)
        } else {
            PhotoListPage(response.photos.photo, null)
        }
}