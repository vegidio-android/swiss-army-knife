package io.vinicius.sak.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals

class TestCollector<T> private constructor()
{
    companion object {
        suspend fun <T> test(scope: CoroutineScope, flow: Flow<T>): TestCollector<T>
        {
            return TestCollector<T>().apply {
                scope.launch {
                    flow.catch { hasError = true }.collect { values.add(it) }
                }.join()
            }
        }
    }

    private var hasError = false
    private val values = mutableListOf<T>()

    fun assertValues(vararg values: T)
    {
        assertEquals(values.toList(), this.values)
    }

    fun assertNoValues()
    {
        assertEquals(emptyList<T>(), this.values)
    }

    fun assertNoErrors()
    {
        assertEquals(hasError, false)
    }

    fun assertValueCount(count: Int)
    {
        assertEquals(values.size, count)
    }
}