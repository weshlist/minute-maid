package io.weshlist.minutemaid.utils.validator

import io.weshlist.minutemaid.result.BaseError

fun <E: BaseError> doValidate(vararg checkers: E?): List<E> {
	return checkers.filterNotNull()
}

inline infix fun <E: BaseError> List<E>.onFailure(block: (E) -> Nothing) =
	when(this) {
		emptyList<E>() -> Unit
		else -> block(this.first())
	}