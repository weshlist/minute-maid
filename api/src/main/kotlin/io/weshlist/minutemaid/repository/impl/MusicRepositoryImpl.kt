package io.weshlist.minutemaid.repository.impl

import io.weshlist.minutemaid.model.mongo.MusicTable
import io.weshlist.minutemaid.repository.Music
import io.weshlist.minutemaid.repository.MusicError
import io.weshlist.minutemaid.repository.MusicMeta
import io.weshlist.minutemaid.repository.MusicRepository
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.result.Result.Failure
import io.weshlist.minutemaid.result.Result.Success
import io.weshlist.minutemaid.result.onFailure
import io.weshlist.minutemaid.result.resultFrom
import io.weshlist.minutemaid.utils.IdGenerator
import mu.KotlinLogging
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class MusicRepositoryImpl(
	private val idGenerator: IdGenerator,
	private val mongoTemplate: MongoTemplate
): MusicRepository {

	private val log = KotlinLogging.logger {}

	override fun getMusic(musicId: String): Result<Music, MusicError> {
		// Get Music
		val getMusicQuery = Query(Criteria.where("musicId").`is`(musicId))
		val currentMusicRow =
			resultFrom {
				mongoTemplate.findOne(getMusicQuery, MusicTable::class.java)
					?: return Failure(MusicError.NotFound(musicId))
			}.onFailure {
				log.error(
					"Error while get music: ${it.reason.message}, \n" +
							it.reason.stackTrace.joinToString("\n")
				)
				return Failure(MusicError.DatabaseError(musicId, it.reason))
			}

		return Success(Music.fromTableRow(currentMusicRow))
	}

	override fun getAddableMusicList(channelId: String): Result<List<Music>, MusicError> {
		TODO()
	}

	override fun addMusic(musicMeta: MusicMeta): Result<Boolean, MusicError> {
		// Get channel
		val newMusic = MusicTable(
			musicId = idGenerator.generateChannelId(),
			musicName = musicMeta.title,
			artist = musicMeta.artist,
			length = musicMeta.length
		)

		// Insert Music
		resultFrom {
			mongoTemplate.insert(newMusic)
		}.onFailure {
			log.error(
				"Error while adding music: ${musicMeta.title}: ${it.reason.message}, \n" +
						it.reason.stackTrace.joinToString("\n")
			)
			return Failure(MusicError.DatabaseError(musicMeta.title, it.reason))
		}

		return Success(true)
	}
}