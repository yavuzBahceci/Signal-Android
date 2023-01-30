package com.app.signal.data.service

import android.util.Log
import com.app.signal.data.BuildConfig
import com.app.signal.domain.model.State
import com.app.signal.domain.service.DataMediator
import com.app.signal.domain.service.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataMediatorImpl @Inject constructor(
    private val errorHandler: ErrorHandler
) : DataMediator {

    override fun <Dto> exec(get: suspend () -> Dto): Flow<State<Dto>> = flow {
        emit(State.Loading(null))

        val state = try {
            val data = get()
            State.Success(data)
        } catch (ex: Throwable) {
            if (BuildConfig.DEBUG) {
                Log.v("DATA_MEDIATOR", "err $ex")
            }
            State.Error(errorHandler.process(ex))
        }

        emit(state)
    }

    override fun <ReturnModel> execSave(save: suspend () -> Flow<ReturnModel>)
            : Flow<State<ReturnModel>> = flow {
        emit(State.Loading(null))

        val state = try {
            save().map {
                val result = it
                if (result is Number) {
                    if (result == -1) {
                        State.Error(errorHandler.process(Throwable()), it)
                    } else {
                        State.Success(it)
                    }
                } else {
                    State.Error(errorHandler.process(Throwable()), it)
                }
            }
        } catch (ex: Throwable) {
            if (BuildConfig.DEBUG) {
                Log.v("DATA_MEDIATOR", "err $ex")
            }
            save().map { State.Error(errorHandler.process(ex), it) }
        }
        emitAll(state)
    }
}