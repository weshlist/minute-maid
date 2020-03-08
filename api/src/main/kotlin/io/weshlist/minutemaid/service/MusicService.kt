package io.weshlist.minutemaid.service

import io.weshlist.minutemaid.repository.MusicError
import io.weshlist.minutemaid.repository.MusicMeta
import io.weshlist.minutemaid.repository.MusicRepository
import io.weshlist.minutemaid.result.Result
import org.springframework.stereotype.Service

@Service
class MusicService(
	private val musicRepository: MusicRepository
) {
	fun add(musicMeta: MusicMeta): Result<Boolean, MusicError> {
		return musicRepository.addMusic(musicMeta)
	}
}