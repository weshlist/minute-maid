package io.weshlist.minutemaid.service

import io.weshlist.minutemaid.model.Music
import io.weshlist.minutemaid.model.MusicMeta
import io.weshlist.minutemaid.repository.MusicRepository
import io.weshlist.minutemaid.result.MusicError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.utils.MusicID
import org.springframework.stereotype.Service

@Service
class MusicService(
	private val musicRepository: MusicRepository
) {

	fun get(musicId: MusicID): Result<Music, MusicError> {
		return musicRepository.getMusic(musicId)
	}

	fun mget(musicIdList: List<MusicID>): Result<List<Music>, MusicError> {
		return musicRepository.mgetMusic(musicIdList)
	}

	fun add(musicMeta: MusicMeta): Result<Boolean, MusicError> {
		return musicRepository.addMusic(musicMeta)
	}
}