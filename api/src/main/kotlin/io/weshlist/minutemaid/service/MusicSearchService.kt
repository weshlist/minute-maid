package io.weshlist.minutemaid.service

import io.weshlist.minutemaid.repository.Music
import io.weshlist.minutemaid.repository.MusicError
import io.weshlist.minutemaid.result.Result
import org.springframework.stereotype.Service


/**
 * TODO
 * Need to implement Search System for finding proper music for each channel
 */

@Service
class MusicSearchService() {
	fun search(channelId: String): Result<List<Music>, MusicError> {
		TODO()
	}
}