package io.weshlist.minutemaid.repository.impl

import io.weshlist.minutemaid.model.Channel
import io.weshlist.minutemaid.model.ChannelMusicStatus
import io.weshlist.minutemaid.model.mongo.ChannelMusicStatusTable
import io.weshlist.minutemaid.model.mongo.ChannelPlaylistTable
import io.weshlist.minutemaid.model.mongo.ChannelTable
import io.weshlist.minutemaid.model.mongo.ChannelUserlistTable
import io.weshlist.minutemaid.repository.ChannelRepository
import io.weshlist.minutemaid.result.ChannelError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.result.Result.Success
import io.weshlist.minutemaid.result.Result.Failure
import io.weshlist.minutemaid.result.onFailure
import io.weshlist.minutemaid.result.resultFrom
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.IdGenerator
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.UserID
import mu.KotlinLogging
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import java.time.LocalDateTime

class ChannelRepositoryImpl(
	private val idGenerator: IdGenerator,
	private val mongoTemplate: MongoTemplate
) : ChannelRepository {
	private val log = KotlinLogging.logger { }

	//TODO: Replace mongo column into Enum value
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
			playlist = listOf(),
			userlist = listOf(userId),
			currentMusicId = null,
			currentMusicStartTime = null
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

	override fun getPlaylist(channelId: ChannelID): Result<List<MusicID>, ChannelError> {
		// Get channel
		val getPlaylistQuery = Query(Criteria.where("channelId").`is`(channelId))
			.apply {
				fields().include("playlist")
			}

		val playlist =
			resultFrom {
				mongoTemplate.findOne(getPlaylistQuery, ChannelPlaylistTable::class.java)
					?: return Failure(ChannelError.NotFound(channelId))
			}.onFailure {
				log.error(
					"Error while get playlist from channel: ${it.reason.message}, \n" +
							it.reason.stackTrace.joinToString("\n")
				)

				return Failure(ChannelError.DatabaseError(channelId, it.reason))
			}.playlist

		return Success(playlist)
	}

	override fun updatePlaylist(channelId: ChannelID, newPlaylist: List<MusicID>): Result<Boolean, ChannelError> {
		val findChannelQuery = Query(Criteria.where("channelId").`is`(channelId))
		val updatePlaylist = Update().apply {
			set("playlist", newPlaylist)
		}

		resultFrom {
			mongoTemplate.updateFirst(findChannelQuery, updatePlaylist, ChannelTable::class.java)
		}.onFailure {
			log.error(
				"Error while updating channel's music playlist from channel: $channelId: ${it.reason.message}, \n" +
						it.reason.stackTrace.joinToString("\n")
			)
			return Failure(ChannelError.DatabaseError(channelId, it.reason))
		}

		return Success(true)
	}

	override fun getMusicStatus(channelId: ChannelID): Result<ChannelMusicStatus, ChannelError> {
		val getCurrentMusicQuery = Query(Criteria.where("channelId").`is`(channelId))

		val musicStatusRow =
			resultFrom {
				mongoTemplate.findOne(getCurrentMusicQuery, ChannelMusicStatusTable::class.java)
					?: return Failure(ChannelError.NotFound(channelId))
			}.onFailure {
				log.error(
					"Error while get current music from channel: ${it.reason.message}, \n" +
							it.reason.stackTrace.joinToString("\n")
				)

				return Failure(ChannelError.DatabaseError(channelId, it.reason))
			}

		return Success(ChannelMusicStatus.fromTableRow(musicStatusRow))
	}

	override fun updateMusicStatus(channelId: ChannelID, nextMusic: MusicID): Result<MusicID, ChannelError> {
		val findChannelQuery = Query(Criteria.where("channelId").`is`(channelId))
		val updateMusicStatus = Update().apply {
			set("currentMusicId", nextMusic)
			pop("playList", Update.Position.FIRST)
			set("currentMusicStartTime", System.currentTimeMillis())
		}

		resultFrom {
			mongoTemplate.updateFirst(findChannelQuery, updateMusicStatus, ChannelTable::class.java)
		}.onFailure {
			log.error(
				"Error while updating channel's music status from channel: $channelId: ${it.reason.message}, \n" +
						it.reason.stackTrace.joinToString("\n")
			)
			return Failure(ChannelError.DatabaseError(channelId, it.reason))
		}

		return Success(nextMusic)
	}

	override fun getM3u8(channelId: ChannelID, currentMusic: MusicID, musicOffset: Long): Result<String, ChannelError> {
		val returnStr = StringBuilder()
		returnStr.append("#EXTM3U\n")
			.append("#EXTINF:10,\n")
			.append("http://127.0.0.1:8080/download/sample_0\n")
			.append("#EXTINF:10,\n")
			.append("http://127.0.0.1:8080/download/sample_1\n")
			.append("#EXTINF:10,\n")
			.append("http://127.0.0.1:8080/download/sample_2\n")
			.append("#EXT-X-ENDLIST")

		return Success(returnStr.toString())
	}

	/*
    // only for test
    private fun patchM3u8(resultM3u8Row: ChannelM3u8Table, channelId: ChannelID): Result<M3u8, ChannelError> {
        val tsList: List<String> = listOf("sample_0.ts", "sample_1.ts", "sample_2.ts", "sample_3.ts", "sample_4.ts")

		val list: MutableList<String> = mutableListOf(resultM3u8Row.streamingFileList[1], resultM3u8Row.streamingFileList[2])
		System.out.println(tsList.indexOf(resultM3u8Row.streamingFileList[2]))
		list.add(tsList[(tsList.indexOf(resultM3u8Row.streamingFileList[2]) + 1) % 5])

		val resetTimestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

		val update = Update()
		update.set("streamingFileList", list)
		update.set("timestamp", resetTimestamp)

        val getChannelQuery = Query(Criteria.where("channelId").`is`(channelId))
        val option = FindAndModifyOptions().returnNew(true)

        val resultM3u8Row =
                resultFrom {
                    mongoTemplate.findAndModify(getChannelQuery, update, option, ChannelM3u8Table::class.java)
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

	override fun getM3u8Format(m3u8: M3u8): Result<String, ChannelError> {
		val returnStr = StringBuilder()
		returnStr.append("#EXTM3U\n")
				.append("#EXTINF:10,\n")
				.append("http://127.0.0.1:8080/download/" + m3u8.streamingFileList[0] + "\n")
				.append("#EXTINF:10,\n")
				.append("http://127.0.0.1:8080/download/" + m3u8.streamingFileList[1] + "\n")
				.append("#EXTINF:10,\n")
				.append("http://127.0.0.1:8080/download/" + m3u8.streamingFileList[2] + "\n")
				.append("#EXT-X-ENDLIST")

		return Success(returnStr.toString())
	}

	 */
}