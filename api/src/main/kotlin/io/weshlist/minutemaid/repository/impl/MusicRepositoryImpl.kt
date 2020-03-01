package io.weshlist.minutemaid.repository.impl

import io.weshlist.minutemaid.client.SomeDataBaseClient
import io.weshlist.minutemaid.repository.Music
import io.weshlist.minutemaid.repository.MusicError
import io.weshlist.minutemaid.repository.MusicRepository
import io.weshlist.minutemaid.result.Result

class MusicRepositoryImpl(
	private val someDataBaseClient: SomeDataBaseClient
): MusicRepository {
	override fun getAddableMusicList(channelId: String): Result<List<Music>, MusicError> {
		TODO()
	}

	override fun addMusic(id: String): Result<Music, MusicError> {
		TODO()
	}
}