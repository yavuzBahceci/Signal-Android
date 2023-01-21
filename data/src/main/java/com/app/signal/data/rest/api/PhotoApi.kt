package com.app.signal.data.rest.api

import com.app.signal.data.dto.response.generic.PhotoResponse
import com.app.signal.data.dto.response.photo.PhotoListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApi {

    @GET("")
    suspend fun searchPhotos(
        @Query("text") searchText: String,
        @Query("page") page: Int
    ): PhotoResponse<PhotoListDto>
}