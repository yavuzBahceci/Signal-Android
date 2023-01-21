package com.app.signal.data.service

import android.content.Context
import com.app.signal.data.R
import com.app.signal.domain.service.AppError
import com.app.signal.domain.service.ErrorHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.nio.charset.Charset
import javax.inject.Inject

class ErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val ctx: Context,
    private val json: Json
) : ErrorHandler {
    private val _interceptedErrorsFlow = MutableSharedFlow<AppError>(
        replay = 0, extraBufferCapacity = 1
    )

    override fun process(error: Throwable): Throwable {
        if (error !is HttpException) {
            return createThrowable(R.string.error_unable_to_connect)
        }

        if (error.code() == 401) {
            // val err = AppError.ExpiredSession

            // _interceptedErrorsFlow.tryEmit(err)

            //return err
        }

        val data = error.response()?.errorBody() ?: return error
        val text = data.source().readString(Charset.defaultCharset())

        return try {
            val body = json.decodeFromString<ErrorBody>(text)
            Throwable(body.message)
        } catch (t: Throwable) {
            return createThrowable(R.string.error_unknown)
        }
    }

    override fun observeInterceptedErrors(): Flow<AppError> {
        return _interceptedErrorsFlow
    }

    private fun createThrowable(msgResId: Int): Throwable {
        return Throwable(ctx.resources.getString(msgResId))
    }

    @Serializable
    data class ErrorBody(
        val errorCode: String? = null,
        val message: String? = null
    )
}