package com.app.signal.data.dto.response.generic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponse<T>(
    val photos: T,
    @SerialName("stat")
    val status: String? = null,
    @SerialName("code")
    val code: Int? = null,
    @SerialName("message")
    val errorMessage: String? = null
)