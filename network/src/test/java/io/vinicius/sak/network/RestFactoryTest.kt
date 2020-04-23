package io.vinicius.sak.network

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.squareup.moshi.Moshi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.logging.HttpLoggingInterceptor.Level
import okhttp3.logging.HttpLoggingInterceptor.Logger
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.converter.moshi.MoshiConverterFactory

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RestFactoryTest
{
    private lateinit var restFactory: RestFactory

    @Before
    fun `Initializing RestFactory`()
    {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val logger = Logger { message -> print(message) }

        restFactory = RestFactory(context).apply {
            converter = MoshiConverterFactory.create(Moshi.Builder().build())
            callAdapter = FlowCallAdapterFactory()
            logHandler = LogHandler(logger, Level.BODY)
        }
    }

    @Test
    fun `1- Fetch data about country Brazil`()
    {
        val service = restFactory.create(CountriesService::class, CountriesService.BASE_URL)

        runBlockingTest {
            val collector = TestCollector.test(this, service.getCountryByCode("BR"))

            collector.assertNoErrors()
            collector.assertValueCount(1)
        }
    }
}