package io.weshlist.minutemaid.repository.impl

import io.weshlist.minutemaid.model.Music
import io.weshlist.minutemaid.model.MusicMeta
import io.weshlist.minutemaid.model.mongo.MusicTable
import io.weshlist.minutemaid.repository.MusicRepository
import io.weshlist.minutemaid.result.MusicError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.result.Result.Failure
import io.weshlist.minutemaid.result.Result.Success
import io.weshlist.minutemaid.result.onFailure
import io.weshlist.minutemaid.result.resultFrom
import io.weshlist.minutemaid.utils.IdGenerator
import io.weshlist.minutemaid.utils.MusicID
import mu.KotlinLogging
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class MusicRepositoryImpl(
	private val idGenerator: IdGenerator,
	private val mongoTemplate: MongoTemplate
): MusicRepository {

	private val log = KotlinLogging.logger {}

	override fun getMusic(musicId: MusicID): Result<Music, MusicError> {
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

	override fun mgetMusic(musicIds: List<MusicID>): Result<List<Music>, MusicError> {
		// Get Music
		val getMusicQuery = Query(Criteria.where("musicId").`in`(musicIds))
		val currentMusicRows =
			resultFrom {
				mongoTemplate.find(getMusicQuery, MusicTable::class.java)
					?: return Failure(MusicError.NotFound(musicIds.first()))
			}.onFailure {
				log.error(
					"Error while get music: ${it.reason.message}, \n" +
							it.reason.stackTrace.joinToString("\n")
				)
				return Failure(MusicError.DatabaseError(musicIds.first(), it.reason))
			}

		return Success(Music.fromTableRows(currentMusicRows))
	}

	override fun getAddableMusicList(channelId: String): Result<List<Music>, MusicError> {
		TODO()
	}

	override fun addMusic(musicMeta: MusicMeta): Result<Boolean, MusicError> {
		// Get Music
		val newMusic = MusicTable(
			musicId = idGenerator.generateMusicId(),
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