package io.weshlist.minutemaid.utils.validator

import io.weshlist.minutemaid.result.BaseError

fun <E: BaseError> validator(vararg checkers: E?): List<E> {
	return checkers.filterNotNull()
}

inline fun <E: BaseError> List<E>.onFailure(block: (E) -> Nothing) =
	when(this) {
		emptyList<E>() -> Unit
		else -> block(this.first())
	}