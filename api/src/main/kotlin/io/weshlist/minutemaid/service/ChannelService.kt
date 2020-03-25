package io.weshlist.minutemaid.service

import io.weshlist.minutemaid.model.Channel
import io.weshlist.minutemaid.model.ChannelMusicStatus
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
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.UserID
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class ChannelService(
	private val channelRepository: ChannelRepository,
	private val userRepository: UserRepository,
	private val musicRepository: MusicRepository
) {
	private val log = KotlinLogging.logger {}

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
		val playlistIds = channelRepository.getPlaylist(channelId).onFailure { return it }
		val playlist = musicRepository.mgetMusic(playlistIds).onFailure { return it }

		return Success(playlist)
	}

	fun getPlaylistCandidates(channelId: ChannelID): Result<List<MusicID>, BaseError> {
		val userlist = channelRepository.getUserList(channelId).onFailure { return it }
		val eachPlaylist = userRepository.mgetPlaylist(userlist).onFailure { return it }

		// TODO: Make algorithm for pick playlist
		val playlist = eachPlaylist.map { it.value }.flatten()

		return Success(playlist)
	}

	// update playlist of channelTable
	fun updatePlaylist(channelId: ChannelID, newMusic: MusicID): Result<Boolean, BaseError> {

		val playlistCandidates = getPlaylistCandidates(channelId).onFailure { return it }

		return channelRepository.updatePlaylist(channelId, playlistCandidates)
	}

	fun getM3u8(channelId: ChannelID): Result<String, BaseError> {

		fun getM3u8WithStatus(musicStatus: ChannelMusicStatus): Result<String, BaseError> {
			// What a weird case!
			if (musicStatus.currentMusic == null || musicStatus.currentMusicStartTime == null) {
				return Result.Failure(ChannelError.NotFound(""))
			}

			val musicOffset = System.currentTimeMillis() - musicStatus.currentMusicStartTime!!
			val m3u8 = channelRepository.getM3u8(channelId, musicStatus.currentMusic!!, musicOffset)
				.onFailure { return it }

			return Success(m3u8)
		}

		fun getM3u8AfterUpdate(channelId: ChannelID, nextMusic: MusicID): Result<String, BaseError> {
			log.debug("Update current music status for channel: $channelId, nextMusic: $nextMusic")

			val updatedCurrentMusic = channelRepository.updateMusicStatus(channelId, nextMusic)
				.onFailure { return it }

			val m3u8 = channelRepository.getM3u8(channelId, updatedCurrentMusic, 0).onFailure { return it }

			return Success(m3u8)
		}

		val musicStatus = channelRepository.getMusicStatus(channelId).onFailure { return it }

		if (musicStatus.currentMusic == null && musicStatus.playlist.isEmpty()) {
			return Success("Empty Music")
		}

		if (musicStatus.currentMusic == null && musicStatus.playlist.isNotEmpty()) {
			return getM3u8AfterUpdate(channelId, musicStatus.playlist.first())
		}

		return getM3u8WithStatus(musicStatus)
	}
}