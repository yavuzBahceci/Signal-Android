package com.app.signal.data.util

import com.app.signal.domain.service.AppError
import com.app.signal.domain.service.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeErrorHandler : ErrorHandler {
    override fun process(error: Throwable): Throwable {
        return error
    }

    override fun observeInterceptedErrors(): Flow<AppError> {
        return flowOf(AppError.BadUrl)
    }
}