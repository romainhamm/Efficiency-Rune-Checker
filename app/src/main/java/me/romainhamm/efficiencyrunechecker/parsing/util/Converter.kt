package me.romainhamm.efficiencyrunechecker.parsing.util

import androidx.annotation.CallSuper
import androidx.arch.core.util.Function
import timber.log.Timber

/**
 * A functional interface that converts a value into another value,
 * possibly with a different type and allows throwing a checked exception.
 *
 * @param <T> the input value type
 * @param <R> the output value type
 */
interface Converter<From, To> : Function<From, To>, (From) -> To {

    /**
     * Apply some calculation to the input value and return some other value.
     * @param t the input value
     * @return the output value
     * @throws Exception on error
     */
    fun convert(t: From): To

    @CallSuper
    fun convertSafe(t: From): To {
        return try {
            convert(t)
        } catch (e: Exception) {
            Timber.wtf(e, "%s failure", this::class.java.simpleName)
            throw e
        }
    }

    override fun apply(t: From): To = convertSafe(t)
    override operator fun invoke(t: From): To = convertSafe(t)
}
