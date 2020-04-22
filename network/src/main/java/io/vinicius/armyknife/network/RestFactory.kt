package io.vinicius.armyknife.network

import android.content.Context
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import okhttp3.logging.HttpLoggingInterceptor.Logger
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Log information
 */
typealias LogHandler = Pair<Logger, Level>

/**
 * Cache information
 */
typealias CachePeriod = Triple<Long, Long, TimeUnit>

class RestFactory private constructor(private val context: Context, private val callAdapter: CallAdapter.Factory,
        private val baseUrl: String, private val logHandler: LogHandler?, private val cachePeriod: CachePeriod?)
{
    // region - Builder
    data class Builder(
            private var context: Context? = null,
            private var callAdapter: CallAdapter.Factory? = null,
            private var baseUrl: String? = null,
            private var logHandler: LogHandler? = LogHandler(Logger.DEFAULT, Level.BASIC),
            private var cachePeriod: CachePeriod? = null)
    {
        // Mandatory
        fun androidContext(value: Context) = apply { this.context = value }
        fun callAdapter(value: CallAdapter.Factory) = apply { this.callAdapter = value }
        fun baseUrl(value: String) = apply { this.baseUrl = value }

        // Optional
        fun logHandler(value: LogHandler?) = apply { this.logHandler = value }
        fun cachePeriod(value: CachePeriod?) = apply { this.cachePeriod = value }

        // Builder
        fun build() = RestFactory(context!!, callAdapter!!, baseUrl!!, logHandler, cachePeriod)
    }
    // endregion

    fun <T> create(clazz: Class<T>): T
    {
        val client = OkHttpClient.Builder()

        // Adding the logger
        logHandler?.let { (logger, level) ->
            val interceptor = HttpLoggingInterceptor(logger).apply { setLevel(level) }
            client.addInterceptor(interceptor)
        }

        // Adding cache support
        cachePeriod?.let { (size, time, unit) ->
            client.cache(Cache(context.cacheDir, size))
                    .addInterceptor(createCachePolicyInterceptor(time, unit))
        }

        // Initialize Retrofit with the OkHttp client
        val retrofit = Retrofit.Builder().client(client.build())
            .addCallAdapterFactory(callAdapter)
            .addConverterFactory(JacksonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()

        return retrofit.create(clazz)
    }

    // region - Interceptors
    private fun createCachePolicyInterceptor(time: Long, unit: TimeUnit): Interceptor
    {
        val seconds = unit.toSeconds(time)

        return Interceptor {
            val request = it.request().newBuilder()
                .header("Cache-Control", "public, max-stale=$seconds")
                .build()

            it.proceed(request)
        }
    }
    // endregion
}