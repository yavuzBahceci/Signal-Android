package com.app.signal.domain.service

import com.app.signal.domain.model.State
import kotlinx.coroutines.flow.Flow


interface DataMediator {

    fun <Dto> exec(
        remote: suspend () -> Dto
    ): Flow<State<Dto>>

    fun <Model> execCache(
        cache: suspend () -> Model,
    ): Flow<State<Model>>

    fun execSave(
        save: suspend () -> Unit
    ): Flow<State<Unit>>
}