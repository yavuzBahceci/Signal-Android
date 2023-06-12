package com.app.signal.data.util

import com.app.signal.domain.model.State
import com.app.signal.domain.service.DataMediator
import com.app.signal.domain.service.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

class DataMediatorFake(
    private val errorHandler: ErrorHandler,
    private val dispatcher: CoroutineDispatcher
) : DataMediator {

    override fun <Dto> exec(get: suspend () -> Dto): Flow<State<Dto>> = flow {
        emit(State.Loading(null))

        val state = try {
            val data = get()
            State.Success(data)
        } catch (ex: Throwable) {
            State.Error(errorHandler.process(ex))
        }

        emit(state)
    }.flowOn(dispatcher)

    override fun <ReturnModel> execSave(save: suspend () -> Flow<ReturnModel>): Flow<State<ReturnModel>> =
        flow {
            emit(State.Loading(null))

            val state = try {
                save().map { State.Success(it) }
            } catch (ex: Throwable) {
                save().map { State.Error(errorHandler.process(ex), it) }
            }

            emitAll(state)
        }.flowOn(dispatcher)
}
