package io.weshlist.minutemaid.service

import io.weshlist.minutemaid.repository.User
import io.weshlist.minutemaid.repository.UserError
import io.weshlist.minutemaid.repository.UserRepository
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.UserID
import org.springframework.stereotype.Service

@Service
class UserService (
	private val userRepository: UserRepository
) {
	fun mgetUser(userIdList: List<UserID>): Result<List<User>, UserError> {
		return userRepository.mgetUser(userIdList)
	}

	fun createUser(userName: String): Result<Boolean, UserError> {

		return userRepository.createUser(userName)
	}

	fun addMusicToPlaylist(userId: UserID, channelId: ChannelID): Result<Boolean, UserError> {

		return userRepository.addMusicToPlaylist(userId, channelId)
	}
}