package com.app.signal.data.service

import android.util.Log
import com.app.signal.data.BuildConfig
import com.app.signal.data.room.AppDatabase
import com.app.signal.domain.model.State
import com.app.signal.domain.service.DataMediator
import com.app.signal.domain.service.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DataMediatorImpl @Inject constructor(
    db: AppDatabase,
    private val errorHandler: ErrorHandler
) : DataMediator {

    override fun <Dto> exec(remote: suspend () -> Dto): Flow<State<Dto>> = flow {
        emit(State.Loading(null))

        val state = try {
            val data = remote()

            State.Success(data)
        } catch (ex: Throwable) {
            if (BuildConfig.DEBUG) {
                Log.v("DATA_MEDIATOR", "err $ex")
            }
            State.Error(errorHandler.process(ex))
        }

        emit(state)
    }

    override fun <Model> execCache(cache: suspend () -> Model): Flow<State<Model>> = flow {
        emit(State.Loading(null))

        val state = try {
            val data = cache()

            State.Success(data)
        } catch (ex: Throwable) {
            if (BuildConfig.DEBUG) {
                Log.v("DATA_MEDIATOR", "err $ex")
            }
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
            if (BuildConfig.DEBUG) {
                Log.v("DATA_MEDIATOR", "err $ex")
            }
            State.Error(errorHandler.process(ex))
        }

        emit(state)
    }


}