package io.weshlist.minutemaid.controller

import io.weshlist.minutemaid.model.MusicMeta
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.UserID

data class JoinChannelRequest(
	val userId: UserID
)

data class RequestMusicReq(
	val userId: UserID,
	val musicId: MusicID
)

data class AddMusicReq (
	val musicMeta: MusicMeta
)