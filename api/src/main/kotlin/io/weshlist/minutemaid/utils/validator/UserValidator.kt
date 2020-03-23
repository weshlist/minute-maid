package io.weshlist.minutemaid.utils.validator

import io.weshlist.minutemaid.result.UserError

object UserValidator {
	fun checkUserId(userId: String): UserError.MalformedName? {
		if (userId == "") {
			return UserError.MalformedName("User id is malformed: $userId ")
		}

		return null
	}
}