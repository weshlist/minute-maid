package io.weshlist.minutemaid.controller.params

import io.weshlist.minutemaid.model.MusicMeta
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.MusicID
import io.weshlist.minutemaid.utils.UserID

data class JoinChannelRequest(
	val userId: UserID
)

data class RequestMusicReq(
	val channelId: ChannelID,
	val musicId: MusicID
)

data class AddMusicReq (
	val musicMeta: MusicMeta
)