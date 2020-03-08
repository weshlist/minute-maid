package io.weshlist.minutemaid.controller

import io.weshlist.minutemaid.repository.MusicMeta
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.UserID

data class RequestMusicReq(
	val userId: UserID,
	val musicId: MusicID
)

data class AddMusicReq (
	val musicMeta: MusicMeta
)