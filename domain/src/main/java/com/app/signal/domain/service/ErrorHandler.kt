package com.app.signal.domain.service

import kotlinx.coroutines.flow.Flow

sealed class FlickrError : Throwable() {
    object SSLError : FlickrError()
    object InvalidSignature : FlickrError()
    object MissingSignature : FlickrError()
    object LoginFailed : FlickrError()
    object UserNotLoggedIn : FlickrError()
    object InvalidApiKey : FlickrError()
    object ServiceCurrentlyUnavailable : FlickrError()
    object WriteOperationFailed : FlickrError()
    object FormatNotFound : FlickrError()
    object MethodNotFound : FlickrError()
    object InvalidSOAPEnvelope : FlickrError()
    object BadUrl : FlickrError()
    object InvalidXMLRPCMethodCall : FlickrError()
}

interface ErrorHandler {
    fun process(error: Throwable): Throwable

    fun observeInterceptedErrors(): Flow<FlickrError>
}