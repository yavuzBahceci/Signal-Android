package com.app.signal.data.repository.store

import com.app.signal.data.dto.response.generic.PhotoResponse
import com.app.signal.data.dto.response.photo.PhotoDto
import com.app.signal.data.repository.photo.store.PhotoRemoteStore
import com.app.signal.data.rest.api.MockPhotoApiResponse
import com.app.signal.domain.form.photo.SearchQueryParams
import com.app.signal.domain.model.PhotoListPage
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class PhotoRetrofitFake(private val mockPhotoApiResponse: MockPhotoApiResponse) : PhotoRemoteStore {

    override suspend fun getPhotos(searchQueryParams: SearchQueryParams): PhotoListPage<PhotoDto> {
        return PhotoListPage(mockPhotoApiResponse.photoResponse.toPhotoDto().photos, 10000)
    }
}

private fun String.toPhotoDto(): PhotoResponse<List<PhotoDto>> {
    val gson = GsonBuilder()
        .create()

    return gson.fromJson(
        this,
        object : TypeToken<PhotoResponse<List<PhotoDto>>>() {}.type
    )
}
