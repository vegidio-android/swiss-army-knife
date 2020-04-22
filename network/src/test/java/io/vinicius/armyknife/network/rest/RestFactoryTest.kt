package io.vinicius.armyknife.network.rest

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import io.vinicius.armyknife.network.CachePeriod
import io.vinicius.armyknife.network.LogHandler
import io.vinicius.armyknife.network.RestFactory
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RestFactoryTest
{
    lateinit var restFactory: RestFactory

    @Before
    fun `Initializing Tests`()
    {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val logger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) = print(message)
        }

        restFactory = RestFactory.Builder()
                .androidContext(context)
                .logHandler(LogHandler(logger, HttpLoggingInterceptor.Level.BASIC))
                .cachePeriod(CachePeriod(10 * 1_024 * 1_024, 1, TimeUnit.DAYS))
                .callAdapter(RxJava2CallAdapterFactory.create())
                .baseUrl("")
                .build()

    }

    @Test
    fun `1- Saves a ParseObject`()
    {

    }
}