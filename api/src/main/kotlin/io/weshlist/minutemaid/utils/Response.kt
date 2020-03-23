package io.weshlist.minutemaid.utils

import io.weshlist.minutemaid.result.Result

import com.fasterxml.jackson.annotation.JsonInclude
import io.weshlist.minutemaid.result.BaseError

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RestApiResponse<T, E>(
	val message: String = "SUCCESS",
	val result: T? = null,
	val error: E? = null
)

fun <T, E> Result<T, E>.toResponse(): RestApiResponse<T, E> {
	return when (this) {
		is Result.Success<T> -> RestApiResponse<T, E>(
			message = "SUCCESS",
			result = value
		)
		is Result.Failure<E> -> RestApiResponse<T, E>(
			// TODO: Error code converter
			message = (reason as? BaseError)?.message ?: "",
			error = reason
		)
	}
}