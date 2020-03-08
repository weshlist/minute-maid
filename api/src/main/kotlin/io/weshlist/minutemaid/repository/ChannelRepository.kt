package io.weshlist.minutemaid.repository

import io.weshlist.minutemaid.model.mongo.ChannelTable
import io.weshlist.minutemaid.result.BaseError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.UserID

interface ChannelRepository {
	fun getChannel(channelName: String): Result<Channel, ChannelError>

	// What if channel creator leave out the channel he / she made?
	fun createChannel(userId: String, channelName: String): Result<Channel, ChannelError>

	fun quitChannel(userId: String, channelId: String): Result<Boolean, ChannelError>
}

data class Channel(
	val channelId: ChannelID,
	val channelName: String,
	val channelCreator: UserID,
	var currentMusic: Music? = null,
	var playlist: List<Music> = emptyList(),
	var userlist: List<UserID> = emptyList(),
	val streamingUri: String = "wesh://streaming_uri/123123"
) {
	companion object {
		fun fromTableRow(table: ChannelTable): Channel {
			return Channel(
				channelId = table.channelId,
				channelName = table.channelName,
				channelCreator = table.channelCreator,
				playlist = table.playlist,
				streamingUri = table.streamingUri
			)
		}
	}
}

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