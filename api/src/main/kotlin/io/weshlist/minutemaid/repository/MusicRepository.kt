package io.weshlist.minutemaid.repository

import io.weshlist.minutemaid.model.mongo.MusicTable
import io.weshlist.minutemaid.result.BaseError
import io.weshlist.minutemaid.result.Result

interface MusicRepository {
	fun getMusic(musicId: String): Result<Music, MusicError>
	fun getAddableMusicList(channelId: String): Result<List<Music>, MusicError>
	fun addMusic(musicMeta: MusicMeta): Result<Boolean, MusicError>
}

// TODO: Re-define Music model
data class Music(
	val musicId: String,
	val musicMeta: MusicMeta
) {
	companion object {
		fun fromTableRow(table: MusicTable): Music {
			return Music(
				musicId = table.musicId,
				musicMeta = MusicMeta(
					title = table.musicName,
					artist = table.artist,
					length = table.length
				)
			)
		}
	}
}

data class MusicMeta(
	val title: String,
	val artist: String,
	val length: Int // ms
)

sealed class MusicError(override val message: String) : BaseError {
	data class NotFound(val name: String) :
		MusicError("No such Music Found: $name")

	data class MalformedName(val name: String) :
		MusicError("Malformed Music name: $name")

	data class DatabaseError(val name: String, val throwable: Throwable) :
		MusicError("Error from Database: ${throwable.message}")

	data class OtherDatabaseError(val name: String, val throwable: Throwable) :
		MusicError("Error from Database: ${throwable.message}")

	// other cases that need different business-specific handling code
}