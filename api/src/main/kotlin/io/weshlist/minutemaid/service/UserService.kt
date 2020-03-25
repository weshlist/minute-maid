package io.weshlist.minutemaid.service

import io.weshlist.minutemaid.model.User
import io.weshlist.minutemaid.repository.ChannelRepository
import io.weshlist.minutemaid.repository.UserRepository
import io.weshlist.minutemaid.result.BaseError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.result.UserError
import io.weshlist.minutemaid.result.onFailure
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.UserID
import org.springframework.stereotype.Service

@Service
class UserService (
	private val channelService: ChannelService,
	private val userRepository: UserRepository
) {
	fun mgetUser(userIdList: List<UserID>): Result<List<User>, UserError> {
		return userRepository.mgetUser(userIdList)
	}

	fun createUser(userName: String): Result<Boolean, UserError> {

		return userRepository.createUser(userName)
	}

	fun requestMusic(channelId: ChannelID, userId: UserID, musicId: MusicID): Result<Boolean, BaseError> {

		userRepository.requestMusic(userId, musicId).onFailure { return it }

		return channelService.updatePlaylist(channelId, musicId)
	}
}