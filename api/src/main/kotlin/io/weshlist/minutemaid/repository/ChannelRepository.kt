package io.weshlist.minutemaid.repository

import io.weshlist.minutemaid.result.BaseError
import io.weshlist.minutemaid.result.Result

interface ChannelRepository {
	fun getChannel(id: String): Result<Channel, ChannelError>
	fun createChannel(id: String): Result<Channel, ChannelError>
}

data class Channel(
	val name: String
)

sealed class ChannelError(override val message: String) : BaseError {
	data class NotFound(val name: String) :
		ChannelError("No such Channel Found: $name")

	data class MalformedName(val name: String) :
		ChannelError("Malformed Channel name: $name")

	data class DatabaseError(val name: String, val throwable: Throwable) :
		ChannelError("Error from Database: ${throwable.message}")

	data class OtherDatabaseError(val name: String, val throwable: Throwable) :
		ChannelError("Error from Database: ${throwable.message}")

	// other cases that need different business-specific handling code
}