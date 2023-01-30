package com.app.signal.data.util

import com.app.signal.domain.model.State
import com.app.signal.domain.service.DataMediator
import com.app.signal.domain.service.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

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

    override fun <ReturnModel> execSave(save: suspend () -> Flow<ReturnModel>): Flow<State<ReturnModel>> =
        flow {
            emit(State.Loading(null))

            val state = try {
                save().map { State.Success(it) }
            } catch (ex: Throwable) {
                save().map { State.Error(errorHandler.process(ex), it) }
            }

            emitAll(state)
        }
}
