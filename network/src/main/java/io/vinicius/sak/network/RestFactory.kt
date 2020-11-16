package io.vinicius.sak.network

import android.content.Context
import android.icu.util.TimeUnit
import android.os.Build
import androidx.annotation.RequiresApi
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
 * Log configuration.
 *
 * @param logger define how the log should be output.
 * @param level  defines the level of information that should be output.
 */
typealias LogHandler = Pair<Logger, Level>

/**
 * Cache configuration.
 *
 * @param time a numeric representation (Long) of time (i.e. 1, 2, 5).
 * @param unit an enumerated representation (TimeUnit) of time (i.e. SECONDS, HOURS, DAYS).
 */
typealias CacheConfig = Pair<Long, TimeUnit>

class RestFactory constructor(private val context: Context)
{
    var converter: Converter.Factory? = null
    var callAdapter: CallAdapter.Factory? = null
    var logHandler: LogHandler? = LogHandler(Logger.DEFAULT, Level.BASIC)
    var cacheSize: Long = 30 * 1_024 * 1_024

    /**
     * Create a new instance of a properly annotated service interface.
     *
     * @param klass the class to be instantiated based on the interface.
     * @param baseUrl the base URL of the REST endpoints.
     * @param cacheConfig the configuration information about the cache.
     */
    fun <T: Any> create(klass: KClass<T>, baseUrl: String, cacheConfig: CacheConfig? = null): T
    {
        val client = OkHttpClient.Builder()

        // Adding the logger
        logHandler?.let { (logger, level) ->
            client.addInterceptor(createLogInterceptor(logger, level))
        }

        // Adding cache support
        cacheConfig?.let { (time, unit) ->
            client.cache(Cache(context.cacheDir, cacheSize)).addInterceptor(createCachePolicyInterceptor(time, unit))
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

    @RequiresApi(Build.VERSION_CODES.N)
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