package io.weshlist.minutemaid.repository

import io.weshlist.minutemaid.model.Channel
import io.weshlist.minutemaid.model.ChannelMusicStatus
import io.weshlist.minutemaid.result.ChannelError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.UserID

interface ChannelRepository {
	fun getChannel(userId: UserID, channelName: String): Result<Channel, ChannelError>

	// What if channel creator leave out the channel he / she made?
	fun createChannel(userId: UserID, channelName: String): Result<Channel, ChannelError>

	fun quitChannel(userId: UserID, channelId: ChannelID): Result<Boolean, ChannelError>

	fun getUserList(channelId: ChannelID): Result<List<UserID>, ChannelError>

	fun getPlaylist(channelId: ChannelID): Result<List<MusicID>, ChannelError>

	fun updatePlaylist(channelId: ChannelID, newPlaylist: List<MusicID>): Result<Boolean, ChannelError>

	fun getMusicStatus(channelId: ChannelID): Result<ChannelMusicStatus, ChannelError>

	fun updateMusicStatus(channelId: ChannelID, nextMusic: MusicID): Result<MusicID, ChannelError>

	fun getM3u8(channelId: ChannelID, currentMusic: MusicID, musicOffset: Long): Result<String, ChannelError>

	/*
	fun getM3u8Format(m3u8: M3u8): Result<String, ChannelError>
	 */
}