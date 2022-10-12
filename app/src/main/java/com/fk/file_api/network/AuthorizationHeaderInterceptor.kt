package com.fk.file_api.network

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder: Request.Builder = chain
            .request()
            .newBuilder()
            .addAuthorizationHeader()
        return chain.proceed(requestBuilder.build())
    }

    private fun Request.Builder.addAuthorizationHeader(): Request.Builder =
        addHeader(AUTHORIZATION_HEADER_NAME, "Basic ${getEncodedCredentials()}")

    private fun getEncodedCredentials(): String {
        return "$USER_NAME:$PASSWORD".encode()
    }

    private fun String.encode(): String {
        return Base64.encodeToString(this.toByteArray(charset("UTF-8")), Base64.NO_WRAP)
    }

    companion object {
        const val AUTHORIZATION_HEADER_NAME = "Authorization"
        const val USER_NAME = "noel"
        const val PASSWORD = "foobar"
    }
}