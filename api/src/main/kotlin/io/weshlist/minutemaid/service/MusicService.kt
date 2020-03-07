package io.weshlist.minutemaid.service

import io.weshlist.minutemaid.repository.MusicError
import io.weshlist.minutemaid.result.Result
import org.springframework.stereotype.Service

@Service
class MusicService() {
	fun add(musicId: String, channelId: String): Result<Boolean, MusicError> {
		TODO()
	}
}