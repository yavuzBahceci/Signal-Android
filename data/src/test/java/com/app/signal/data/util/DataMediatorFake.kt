package com.app.signal.data.util

import com.app.signal.domain.model.State
import com.app.signal.domain.service.DataMediator
import com.app.signal.domain.service.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataMediatorFake(private val errorHandler: ErrorHandler) : DataMediator {

    override fun <Dto> exec(get: suspend () -> Dto): Flow<State<Dto>> = flow {
        emit(State.Loading(null))

        val state = try {
            val data = get()
            State.Success(data)
        } catch (ex: Throwable) {
            State.Error(errorHandler.process(ex))
        }

        emit(state)
    }

    override fun execSave(save: suspend () -> Unit): Flow<State<Unit>> = flow {
        emit(State.Loading(null))

        val state = try {
            val data = save()

            State.Success(data)
        } catch (ex: Throwable) {
            State.Error(errorHandler.process(ex))
        }

        emit(state)
    }
}
