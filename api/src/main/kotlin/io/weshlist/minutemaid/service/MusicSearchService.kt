package io.weshlist.minutemaid.service

import io.weshlist.minutemaid.model.Music
import io.weshlist.minutemaid.result.MusicError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.utils.ChannelID
import org.springframework.stereotype.Service


/**
 * TODO
 * Need to implement Search System for finding proper music for each channel
 * Implement using Elastic Search or something.
 */

@Service
class MusicSearchService() {
	fun search(channelId: ChannelID, keyword: String): Result<List<Music>, MusicError> {
		TODO()
	}

	fun searchAll(channelId: ChannelID): Result<List<Music>, MusicError> {
		TODO()
	}
}