package io.weshlist.minutemaid.utils

import io.weshlist.minutemaid.result.Result

import com.fasterxml.jackson.annotation.JsonInclude
import io.weshlist.minutemaid.result.BaseError

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RestApiResponse<T, E>(
	val code: Int = 0,
	val message: String = "SUCCESS",
	val result: T? = null,
	val error: E? = null
)

fun <T, E> Result<T, E>.toResponse(): RestApiResponse<T, E> {
	return when (this) {
		is Result.Success<T> -> RestApiResponse<T, E>(
			code = 200,
			message = "SUCCESS",
			result = value
		)
		is Result.Failure<E> -> RestApiResponse<T, E>(
			// TODO: Error code converter
			code = 500,
			message = (reason as? BaseError)?.message ?: "",
			error = reason
		)
	}
}