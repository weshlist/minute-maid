package io.weshlist.minutemaid.repository

import io.weshlist.minutemaid.model.Channel
import io.weshlist.minutemaid.model.M3u8
import io.weshlist.minutemaid.result.ChannelError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.UserID

interface ChannelRepository {
	fun getChannel(userId: UserID, channelName: String): Result<Channel, ChannelError>

	// What if channel creator leave out the channel he / she made?
	fun createChannel(userId: UserID, channelName: String): Result<Channel, ChannelError>

	fun quitChannel(userId: UserID, channelId: ChannelID): Result<Boolean, ChannelError>

	fun getUserList(channelId: ChannelID): Result<List<UserID>, ChannelError>

	fun getM3u8(channelId: ChannelID): Result<M3u8, ChannelError>

	fun patchM3u8(channelId: ChannelID): Result<M3u8, ChannelError>

	fun createM3u8(channelId: ChannelID): Result<M3u8, ChannelError>
}