package io.weshlist.minutemaid.repository.impl

import io.weshlist.minutemaid.model.mongo.ChannelTable
import io.weshlist.minutemaid.repository.Channel
import io.weshlist.minutemaid.repository.ChannelError
import io.weshlist.minutemaid.repository.ChannelRepository
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.result.Result.Success
import io.weshlist.minutemaid.result.Result.Failure
import io.weshlist.minutemaid.result.onFailure
import io.weshlist.minutemaid.result.resultFrom
import io.weshlist.minutemaid.utils.IdGenerator
import io.weshlist.minutemaid.utils.UserID
import mu.KotlinLogging
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class ChannelRepositoryImpl(
	private val idGenerator: IdGenerator,
	private val mongoTemplate: MongoTemplate
) : ChannelRepository {
	private val log = KotlinLogging.logger { }

	override fun getChannel(channelName: String): Result<Channel, ChannelError> {

		// Get channel
		val getChannelQuery = Query(Criteria.where("channelName").`is`(channelName))
		val resultChannelRow =
			resultFrom {
				mongoTemplate.findOne(getChannelQuery, ChannelTable::class.java)
					?: return Failure(ChannelError.NotFound(channelName))
			}.onFailure {
				log.error(
					"Error while get channel: ${it.reason.message}, \n" +
							it.reason.stackTrace.joinToString("\n")
				)
				return Failure(ChannelError.DatabaseError(channelName, it.reason))
			}

		return Success(Channel.fromTableRow(resultChannelRow))
	}

	override fun createChannel(userId: UserID, channelName: String): Result<Channel, ChannelError> {

		// Get channel
		val newChannel = ChannelTable(
			channelId = idGenerator.generateChannelId(),
			channelName = channelName,
			channelCreator = userId,
			currentMusicId = null,
			playlist = listOf(),
			userlist = listOf(userId),
			streamingUri = ""
		)

		// Insert Channel
		resultFrom {
			mongoTemplate.insert(newChannel)
		}.onFailure {
			log.error(
				"Error while creating channel $channelName: ${it.reason.message}, \n" +
						it.reason.stackTrace.joinToString("\n")
			)
			return Failure(ChannelError.DatabaseError(channelName, it.reason))
		}

		// Duplicate channel name!
		return Success(Channel.fromTableRow(newChannel))
	}

	override fun quitChannel(userId: String, channelId: String): Result<Boolean, ChannelError> {
		TODO("not implemented")
	}
}