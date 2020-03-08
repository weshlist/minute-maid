package io.weshlist.minutemaid.utils.validator

import io.weshlist.minutemaid.repository.MusicError
import io.weshlist.minutemaid.utils.MusicID

object MusicValidator {
	fun checkMusicId(musicId: MusicID): MusicError.MalformedName? {
		if (musicId == "") {
			return MusicError.MalformedName("MusicId is malformed: $musicId ")
		}
		return null
	}

	fun checkMusicTitle(title: String): MusicError.MalformedName? {
		if (title == "") {
			return MusicError.MalformedName("MusicId is malformed: $title ")
		}
		return null
	}
}