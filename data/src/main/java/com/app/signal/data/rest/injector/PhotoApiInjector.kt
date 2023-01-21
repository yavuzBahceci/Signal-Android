package com.app.signal.data.rest.injector

import com.app.signal.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

private object HeaderKey {
    const val METHOD = "method"
    const val API_KEY = "api_key"
    const val FORMAT = "format"
    const val NO_JSON = "nojsoncallback"
    const val PER_PAGE = "per_page"
}

private object HeaderValue {
    const val methodValue = "flickr.photos.search"
    const val apiKeyValue = BuildConfig.FLICKR_API_KEY
    const val formatValue = "json"
    const val noJsonCallbackValue = "?"
    const val perPageValue = 25
}

class PhotoApiInjector : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request()
            .newBuilder()
            .header(HeaderKey.METHOD, HeaderValue.methodValue)
            .header(HeaderKey.API_KEY, HeaderValue.apiKeyValue)
            .header(HeaderKey.FORMAT, HeaderValue.formatValue)
            .header(HeaderKey.NO_JSON, HeaderValue.noJsonCallbackValue)
            .header(HeaderKey.PER_PAGE, HeaderValue.perPageValue.toString())


        return chain.proceed(builder.build())
    }
}
