package com.example.ajiekc.karoon.di.provider

import android.annotation.SuppressLint
import com.example.ajiekc.karoon.BuildConfig
import com.example.ajiekc.karoon.api.LoggingInterceptor
import com.example.ajiekc.karoon.api.youtube.AuthInterceptor
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

class HttpClientProvider @Inject constructor(val authInterceptor: AuthInterceptor) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient {
        val builder = OkHttpClient.Builder().also {
            it.readTimeout(20, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                val interceptor = LoggingInterceptor()
                it.addInterceptor(interceptor)
                it.addNetworkInterceptor(authInterceptor)

                val trustManager = createDevelopTrustManager()
                val sslSocketFactory = createDevelopSslSocketFactory(trustManager)
                it.sslSocketFactory(sslSocketFactory, trustManager)
            }
        }

        return builder.build()
    }

    @SuppressLint("TrustAllX509TrustManager")
    private fun createDevelopTrustManager(): X509TrustManager {
        return object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
    }

    private fun createDevelopSslSocketFactory(trustManager: X509TrustManager): SSLSocketFactory {
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(trustManager), SecureRandom())

        return sslContext.socketFactory
    }
}