package com.app.signal.domain.form.photo

data class SearchQueryParams(
    val searchText: String,
    val page: Long? = 0
)