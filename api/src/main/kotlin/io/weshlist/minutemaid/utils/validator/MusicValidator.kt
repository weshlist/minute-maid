package io.weshlist.minutemaid.utils.validator

import io.weshlist.minutemaid.repository.MusicError

object MusicValidator {
	fun checkMusicId(musicId: String): MusicError.MalformedName? {

		if (musicId == "FINE") {
			return null
		}

		return MusicError.MalformedName("MusicId is malformed: $musicId ")
	}
}