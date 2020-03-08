package io.weshlist.minutemaid.repository

import io.weshlist.minutemaid.model.mongo.UserTable
import io.weshlist.minutemaid.result.BaseError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.UserID

interface UserRepository {
	fun createUser(userName: String): Result<Boolean, UserError>
	fun deleteUser(userId: UserID): Result<Boolean, UserError>

	fun mgetUser(userIdList: List<UserID>): Result<List<User>, UserError>
	fun addMusicToPlaylist(userId: UserID, musicId: MusicID): Result<Boolean, UserError>
}

data class User(
	val userId: UserID,
	val userName: String,
	val profileImageUri: String = "wesh://profile_image_uri",
	var playlist: List<MusicID> = emptyList()
) {
	companion object {
		fun fromTableRow(table: UserTable): User {
			return User(
				userId = table.userId,
				userName = table.userName,
				profileImageUri = table.profileImageUri,
				playlist = table.playlist
			)
		}

		fun fromTableRows(tables: List<UserTable>): List<User> {
			return tables.map(::fromTableRow)
		}
	}
}

sealed class UserError(override val message: String) : BaseError {
	data class NotFound(val name: String) :
		UserError("No such Channel Found: $name")

	data class MalformedName(val name: String) :
		UserError("Malformed Channel name: $name")

	data class DatabaseError(val name: String, val throwable: Throwable) :
		UserError("Error from Database: ${throwable.message}")

	data class OtherDatabaseError(val name: String, val throwable: Throwable) :
		UserError("Error from Database: ${throwable.message}")

	// other cases that need different business-specific handling code
}