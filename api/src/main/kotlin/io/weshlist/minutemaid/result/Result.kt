// Code from: https://github.com/npryce/result4k

package io.weshlist.minutemaid.result

import kotlin.reflect.KClass

/**
 * A result of a computation that can succeed or fail.
 */
sealed class Result<out T, out E> {
	data class Success<out T>(val value: T) : Result<T, Nothing>()
	data class Failure<out E>(val reason: E) : Result<Nothing, E>()
}

/**
 * Call a function and wrap the result in a Result, catching any Exception and returning it as Err value.
 */
inline fun <T> resultFrom(block: () -> T): Result<T, Exception> =
	try {
		Result.Success(block())
	} catch (x: Exception) {
		Result.Failure(x)
	}

/**
 * Map a function over the _value_ of a successful Result.
 */
inline fun <T, Tʹ, E> Result<T, E>.map(f: (T) -> Tʹ): Result<Tʹ, E> =
	flatMap { value -> Result.Success(f(value)) }

/**
 * Flat-map a function over the _value_ of a successful Result.
 */
inline fun <T, Tʹ, E> Result<T, E>.flatMap(f: (T) -> Result<Tʹ, E>): Result<Tʹ, E> =
	when (this) {
		is Result.Success<T> -> f(value)
		is Result.Failure<E> -> this
	}

/**
 * Unwrap a Result in which both the success and failure values have the same type, returning a plain value.
 */
fun <T> Result<T, T>.get() = when (this) {
	is Result.Success<T> -> value
	is Result.Failure<T> -> reason
}

/**
 * Unwrap a Result, by returning the success value or calling _block_ on failure to abort from the current function.
 */
inline infix fun <T, E> Result<T, E>.onFailure(block: (Result.Failure<E>) -> Nothing): T =
	when (this) {
		is Result.Success<T> -> value
		is Result.Failure<E> -> block(this)
	}


inline fun <T, reified E: Any, Eʹ: E, Eʹʹ: E> Result<T, E>.onFailureWhen(case: KClass<Eʹ>, f: () -> Result<T, Eʹʹ>): Result<T, E> =
	if (case is E) f() else this

fun <T: Any> T.toSuccess(): Result<T, Nothing> {
	return Result.Success(this)
}
