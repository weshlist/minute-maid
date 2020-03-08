package io.weshlist.minutemaid.repository.impl

import io.weshlist.minutemaid.model.User
import io.weshlist.minutemaid.model.mongo.UserPlaylistTable
import io.weshlist.minutemaid.model.mongo.UserTable
import io.weshlist.minutemaid.repository.UserRepository
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.result.Result.Failure
import io.weshlist.minutemaid.result.Result.Success
import io.weshlist.minutemaid.result.UserError
import io.weshlist.minutemaid.result.onFailure
import io.weshlist.minutemaid.result.resultFrom
import io.weshlist.minutemaid.utils.IdGenerator
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.UserID
import mu.KotlinLogging
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update

class UserRepositoryImpl(
	private val idGenerator: IdGenerator,
	private val mongoTemplate: MongoTemplate
) : UserRepository {

	private val log = KotlinLogging.logger {}

	/**
	 * How to process duplicate userName?
	 */
	override fun createUser(userName: String): Result<Boolean, UserError> {
		// Get User
		val newUser = UserTable(
			userId = idGenerator.generateUserId(),
			userName = userName,
			profileImageUri = "",
			playlist = listOf()
		)

		// Insert User
		resultFrom {
			mongoTemplate.insert(newUser)
		}.onFailure {
			log.error(
				"Error while adding music: ${userName}: ${it.reason.message}, \n" +
						it.reason.stackTrace.joinToString("\n")
			)
			return Failure(UserError.DatabaseError(userName, it.reason))
		}

		return Success(true)
	}

	override fun deleteUser(userId: UserID): Result<Boolean, UserError> {
		TODO()
	}

	override fun mgetUser(userIds: List<UserID>): Result<List<User>, UserError> {
		if (userIds.isEmpty()) {
			return Failure(UserError.MalformedName("Request id list is empty"))
		}

		val findUserQuery = Query(Criteria.where("userId").`in`(userIds))

		val resultUserRow =
			resultFrom {
				mongoTemplate.find(findUserQuery, UserTable::class.java)
			}.onFailure {
				log.error(
					"Error while adding getting Users: ${it.reason.message}, \n" +
							it.reason.stackTrace.joinToString("\n")
				)
				return Failure(UserError.DatabaseError(userIds.first(), it.reason))
			}

		return Success(User.fromTableRows(resultUserRow))
	}

	override fun requestMusic(userId: UserID, musicId: MusicID): Result<Boolean, UserError> {
		val findUserQuery = Query(Criteria.where("userId").`is`(userId))
		val updatePlaylist = Update().apply {
			push("playlist", musicId)
		}
		val option = FindAndModifyOptions().returnNew(true)

		resultFrom {
			mongoTemplate.findAndModify(findUserQuery, updatePlaylist, option, UserTable::class.java)
		}.onFailure {
			log.error(
				"Error while adding music to User's playlist: $userId: ${it.reason.message}, \n" +
						it.reason.stackTrace.joinToString("\n")
			)
			return Failure(UserError.DatabaseError(userId, it.reason))
		}

		return Success(true)
	}

	override fun mgetPlaylist(userIds: List<UserID>): Result<Map<UserID, List<MusicID>>, UserError> {
		if (userIds.isEmpty()) {
			return Failure(UserError.MalformedName("Request id list is empty"))
		}

		val findUserQuery = Query(Criteria.where("userId").`in`(userIds))
			.apply {
				fields().include("playlist")
			}

		val playlist =
			resultFrom {
				mongoTemplate.find(findUserQuery, UserPlaylistTable::class.java)
			}.onFailure {
				log.error(
					"Error while adding getting Users: ${it.reason.message}, \n" +
							it.reason.stackTrace.joinToString("\n")
				)
				return Failure(UserError.DatabaseError(userIds.first(), it.reason))
			}.map { it.userId to it.playlist }.toMap()

		return Success(playlist)
	}
}