package io.weshlist.minutemaid.repository

import io.weshlist.minutemaid.model.User
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.result.UserError
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.UserID

interface UserRepository {
	fun createUser(userName: String): Result<Boolean, UserError>
	fun deleteUser(userId: UserID): Result<Boolean, UserError>

	fun mgetUser(userIds: List<UserID>): Result<List<User>, UserError>
	fun requestMusic(userId: UserID, musicId: MusicID): Result<Boolean, UserError>

	fun mgetPlaylist(userIds: List<UserID>): Result<Map<UserID, List<MusicID>>, UserError>
}