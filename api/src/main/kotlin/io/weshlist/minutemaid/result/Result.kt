// Code from: https://github.com/npryce/result4k

package io.weshlist.minutemaid.result

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
 * Flat-map a function over the _reason_ of a unsuccessful Result.
 */
inline fun <T, E, Eʹ> Result<T, E>.flatMapFailure(f: (E) -> Result<T, Eʹ>): Result<T, Eʹ> = when (this) {
	is Result.Success<T> -> this
	is Result.Failure<E> -> f(reason)
}

/**
 * Map a function over the _reason_ of an unsuccessful Result.
 */
inline fun <T, E, Eʹ> Result<T, E>.mapFailure(f: (E) -> Eʹ): Result<T, Eʹ> =
	flatMapFailure { reason -> Result.Failure(f(reason)) }

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
inline fun <T, E> Result<T, E>.onFailure(block: (Result.Failure<E>) -> Nothing): T = when (this) {
	is Result.Success<T> -> value
	is Result.Failure<E> -> block(this)
}

/**
 * Unwrap a Result by returning the success value or calling _failureToValue_ to mapping the failure reason to a plain value.
 */
inline fun <S, T : S, U : S, E> Result<T, E>.recover(errorToValue: (E) -> U): S =
	mapFailure(errorToValue).get()

/**
 * Perform a side effect with the success value.
 */
inline fun <T, E> Result<T, E>.peek(f: (T) -> Unit) =
	apply { if (this is Result.Success<T>) f(value) }

/**
 * Perform a side effect with the failure reason.
 */
inline fun <T, E> Result<T, E>.peekFailure(f: (E) -> Unit) =
	apply { if (this is Result.Failure<E>) f(reason) }