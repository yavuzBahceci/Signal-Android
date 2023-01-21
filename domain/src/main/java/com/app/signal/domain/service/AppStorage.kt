package com.app.signal.domain.service

import kotlinx.coroutines.flow.Flow

interface AppStorage {

    var previousSearches: List<String>

    fun observePreviousSearches(): Flow<List<String>>
    fun reset()
}