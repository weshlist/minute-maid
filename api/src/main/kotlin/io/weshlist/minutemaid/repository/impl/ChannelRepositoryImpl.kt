package io.weshlist.minutemaid.repository.impl

import io.weshlist.minutemaid.model.Channel
import io.weshlist.minutemaid.model.M3u8
import io.weshlist.minutemaid.model.mongo.ChannelTable
import io.weshlist.minutemaid.model.mongo.ChannelUserlistTable
import io.weshlist.minutemaid.model.mongo.M3u8Table
import io.weshlist.minutemaid.repository.ChannelRepository
import io.weshlist.minutemaid.result.ChannelError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.result.Result.Success
import io.weshlist.minutemaid.result.Result.Failure
import io.weshlist.minutemaid.result.onFailure
import io.weshlist.minutemaid.result.resultFrom
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.IdGenerator
import io.weshlist.minutemaid.utils.UserID
import mu.KotlinLogging
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChannelRepositoryImpl(
	private val idGenerator: IdGenerator,
	private val mongoTemplate: MongoTemplate
) : ChannelRepository {
	private val log = KotlinLogging.logger { }

	override fun getChannel(userId: UserID, channelName: String): Result<Channel, ChannelError> {

		// Get channel
		val getChannelQuery = Query(Criteria.where("channelName").`is`(channelName))
		val updateUserlist = Update().apply {
			push("userlist", userId)
		}
		val option = FindAndModifyOptions().returnNew(true)

		val resultChannelRow =
			resultFrom {
				mongoTemplate.findAndModify(getChannelQuery, updateUserlist, option, ChannelTable::class.java)
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
        val now = LocalDateTime.now()
        val newChannel = ChannelTable(
                channelId = idGenerator.generateChannelId(),
                channelName = channelName,
                channelCreator = userId,
                currentMusicId = null,
                playlist = listOf(),
                userlist = listOf(userId),
                streamingUri = "",
                streamingFileList = listOf("sample_0.ts", "sample_1.ts", "sample_2.ts"),
                timestamp = now.format(DateTimeFormatter.ISO_DATE_TIME)
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

	override fun getUserList(channelId: ChannelID): Result<List<UserID>, ChannelError> {
		// Get channel
		val getUserlistQuery = Query(Criteria.where("channelId").`is`(channelId))
			.apply {
				fields().include("userlist")
			}

		val userlist =
			resultFrom {
				mongoTemplate.findOne(getUserlistQuery, ChannelUserlistTable::class.java)
					?: return Failure(ChannelError.NotFound(channelId))
			}.onFailure {
				log.error(
					"Error while get channel: ${it.reason.message}, \n" +
							it.reason.stackTrace.joinToString("\n")
				)
				return Failure(ChannelError.DatabaseError(channelId, it.reason))
			}.userlist

		return Success(userlist)
	}

	override fun getM3u8(channelId: ChannelID): Result<M3u8, ChannelError> {
		val getM3u8Query = Query(Criteria.where("channelId").`is`(channelId))

		val resultM3u8Row =
			resultFrom {
				mongoTemplate.findOne(getM3u8Query, M3u8Table::class.java)
					?: return Failure(ChannelError.NotFound(channelId))
			}.onFailure {
				log.error(
						"Error while get channel: ${it.reason.message}, \n" +
								it.reason.stackTrace.joinToString("\n")
				)
				return Failure(ChannelError.DatabaseError(channelId, it.reason))
			}

		val timestamp = LocalDateTime.parse(resultM3u8Row.timestamp, DateTimeFormatter.ISO_DATE_TIME).plusSeconds(10)
		val now = LocalDateTime.now()

        if (now.isAfter(timestamp.plusSeconds(10))) {
            return patchM3u8(resultM3u8Row, channelId)
        }

		return Success(M3u8.fromTableRow(resultM3u8Row))
	}

    // only for test
    private fun patchM3u8(resultM3u8Row: M3u8Table, channelId: ChannelID): Result<M3u8, ChannelError> {
        val tsList: List<String> = listOf("sample_0.ts", "sample_1.ts", "sample_2.ts", "sample_3.ts", "sample_4.ts")

		val list: MutableList<String> = mutableListOf(resultM3u8Row.streamingFileList[1], resultM3u8Row.streamingFileList[2])
		list.add(tsList[tsList.indexOf(resultM3u8Row.streamingFileList[2]) + 1 % 5])

		val resetTimestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

		val update = Update()
		update.set("streamingFileList", list)
		update.set("timestamp", resetTimestamp)

        val getChannelQuery = Query(Criteria.where("channelId").`is`(channelId))
        val option = FindAndModifyOptions().returnNew(true)

        val resultM3u8Row =
                resultFrom {
                    mongoTemplate.findAndModify(getChannelQuery, update, option, M3u8Table::class.java)
                            ?: return Failure(ChannelError.NotFound(channelId))
                }.onFailure {
                    log.error(
                            "Error while patch m3u8: ${it.reason.message}, \n" +
                                    it.reason.stackTrace.joinToString("\n")
                    )
                    return Failure(ChannelError.DatabaseError(channelId, it.reason))
                }

        return Success(M3u8.fromTableRow(resultM3u8Row))
    }

}