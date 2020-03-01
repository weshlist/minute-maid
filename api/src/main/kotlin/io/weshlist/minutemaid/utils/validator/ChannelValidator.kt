package io.weshlist.minutemaid.utils.validator

import io.weshlist.minutemaid.repository.ChannelError

object ChannelValidator {
	fun checkChannelId(channelId: String): ChannelError.MalformedName? {

		if (channelId == "FINE") {
			return null
		}

		return ChannelError.MalformedName("Channel is malformed: $channelId ")
	}

	fun checkUserId(userId: String): ChannelError.MalformedName? {

		if (userId == "FINE") {
			return null
		}

		return ChannelError.MalformedName("User id is malformed: $userId ")
	}
}