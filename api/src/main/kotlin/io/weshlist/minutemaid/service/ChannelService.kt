package io.weshlist.minutemaid.service

import io.weshlist.minutemaid.model.Channel
import io.weshlist.minutemaid.model.Music
import io.weshlist.minutemaid.repository.ChannelRepository
import io.weshlist.minutemaid.repository.MusicRepository
import io.weshlist.minutemaid.repository.UserRepository
import io.weshlist.minutemaid.result.BaseError
import io.weshlist.minutemaid.result.ChannelError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.result.Result.Success
import io.weshlist.minutemaid.result.onFailure
import io.weshlist.minutemaid.result.onFailureWhen
import io.weshlist.minutemaid.result.toSuccess
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.UserID
import org.springframework.stereotype.Service

@Service
class ChannelService(
	private val channelRepository: ChannelRepository,
	private val userRepository: UserRepository,
	private val musicRepository: MusicRepository
) {

	fun join(userId: UserID, channelName: String): Result<Channel, ChannelError> {

		return channelRepository.getChannel(userId, channelName)
			.onFailureWhen(ChannelError.NotFound::class) {
				channelRepository.createChannel(userId, channelName)
			}
			.onFailure {
				return it
			}
			.toSuccess()
	}

	fun quit(userId: UserID, channelId: ChannelID): Result<Boolean, ChannelError> {
		return channelRepository.quitChannel(userId, channelId)
	}

	// TODO: How to handle duplicate music request by user?
	fun getPlaylist(channelId: ChannelID): Result<List<Music>, BaseError> {
		val userlist = channelRepository.getUserList(channelId).onFailure { return it }
		val eachPlaylist = userRepository.mgetPlaylist(userlist).onFailure { return it }

		// TODO: Make algorithm for pick playlist
		val playlist = musicRepository.mgetMusic(
			eachPlaylist.map{it.value}.flatten()
		).onFailure { return it }

		return Success(playlist)
	}

}