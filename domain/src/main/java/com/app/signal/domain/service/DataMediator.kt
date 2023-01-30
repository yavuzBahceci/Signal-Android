package com.app.signal.domain.service

import com.app.signal.domain.model.State
import kotlinx.coroutines.flow.Flow


interface DataMediator {

    fun <Model> exec(
        get: suspend () -> Model
    ): Flow<State<Model>>

    fun <ReturnModel> execSave(
        save: suspend () -> Flow<ReturnModel>
    ): Flow<State<ReturnModel>>
}