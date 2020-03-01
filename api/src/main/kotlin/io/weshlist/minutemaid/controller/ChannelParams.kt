package io.weshlist.minutemaid.controller

import org.springframework.web.bind.annotation.PathVariable

object ChannelParams {
	data class JoinRequest(
		@PathVariable val channelId: String
	)
	
	data class JoinResponse(
		
	)

	data class SearchMusicRequest(
		@PathVariable val channelId: String,
		@PathVariable val userId: String
	)
}