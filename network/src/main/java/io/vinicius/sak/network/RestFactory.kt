package io.vinicius.sak.network

import android.content.Context
import android.icu.util.TimeUnit
import io.vinicius.sak.util.ktx.toSeconds
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import okhttp3.logging.HttpLoggingInterceptor.Logger
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import kotlin.reflect.KClass

/**
 * Log configuration
 *
 * @param logger define how the log should be output.
 * @param level  defines the level of information that should be output.
 */
typealias LogHandler = Pair<Logger, Level>

/**
 * Cache configuration
 *
 * @param size the cache size, in bytes.
 * @param time a numeric representation (Long) of time (i.e. 1, 2, 5).
 * @param unit an enumerated representation (TimeUnit) of time (i.e. SECONDS, HOURS, DAYS).
 */
typealias CacheConfig = Triple<Long, Long, TimeUnit>

class RestFactory constructor(private val context: Context)
{
    var converter: Converter.Factory? = null
    var callAdapter: CallAdapter.Factory? = null
    var cacheConfig: CacheConfig? = null
    var logHandler: LogHandler? = LogHandler(Logger.DEFAULT, Level.BASIC)

    fun <T: Any> create(klass: KClass<T>, baseUrl: String): T
    {
        val client = OkHttpClient.Builder()

        // Adding the logger
        logHandler?.let { (logger, level) ->
            client.addInterceptor(createLogInterceptor(logger, level))
        }

        // Adding cache support
        cacheConfig?.let { (size, time, unit) ->
            client.cache(Cache(context.cacheDir, size)).addInterceptor(createCachePolicyInterceptor(time, unit))
        }

        // Initialize Retrofit with the OkHttp client
        val retrofit = Retrofit.Builder()
                .client(client.build())
                .baseUrl(baseUrl)

        // Adding the converter
        converter?.let {
            retrofit.addConverterFactory(it)
        }

        // Adding the call adapter
        callAdapter?.let {
            retrofit.addCallAdapterFactory(it)
        }

        return retrofit.baseUrl(baseUrl)
                .build()
                .create(klass.java)
    }

    // region - Interceptors
    private fun createLogInterceptor(logger: Logger, level: Level): Interceptor
    {
        return HttpLoggingInterceptor(logger).apply { setLevel(level) }
    }

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