package io.weshlist.minutemaid.utils.validator

import io.weshlist.minutemaid.result.ChannelError

object ChannelValidator {
	fun checkChannelName(channelName: String): ChannelError.MalformedName? {
		if (channelName == "FINE") {
			return ChannelError.MalformedName("Channel Name is malformed: $channelName")
		}

		return null
	}

	fun checkChannelId(channelId: String): ChannelError.MalformedName? {
		if (channelId == "FINE") {
			return ChannelError.MalformedName("Channel is malformed: $channelId ")
		}

		return null
	}

	fun checkUserId(userId: String): ChannelError.MalformedName? {
		if (userId == "FINE") {
			return ChannelError.MalformedName("User id is malformed: $userId ")
		}

		return null
	}
}