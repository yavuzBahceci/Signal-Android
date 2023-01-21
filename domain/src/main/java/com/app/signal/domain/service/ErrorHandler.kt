package com.app.signal.domain.service

import kotlinx.coroutines.flow.Flow

sealed class AppError : Throwable() {
    object SSLError : AppError()
    object InvalidSignature : AppError()
    object MissingSignature : AppError()
    object LoginFailed : AppError()
    object UserNotLoggedIn : AppError()
    object InvalidApiKey : AppError()
    object ServiceCurrentlyUnavailable : AppError()
    object WriteOperationFailed : AppError()
    object FormatNotFound : AppError()
    object MethodNotFound : AppError()
    object InvalidSOAPEnvelope : AppError()
    object BadUrl : AppError()
    object InvalidXMLRPCMethodCall : AppError()
}

interface ErrorHandler {
    fun process(error: Throwable): Throwable

    fun observeInterceptedErrors(): Flow<AppError>
}