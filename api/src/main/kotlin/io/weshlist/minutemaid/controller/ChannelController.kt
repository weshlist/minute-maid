package io.weshlist.minutemaid.controller

import io.weshlist.minutemaid.repository.Channel
import io.weshlist.minutemaid.service.ChannelService
import io.weshlist.minutemaid.service.MusicSearchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/*
api
1 . room - id empty - > create
            exsists -> enter
2. music - 추가 가능한 음악 목록 불러오기
            - 음악 위시리스트에 추가
            - 내가 추가한 음악 리스트 불러오기
 */
@RequestMapping("/channel")
open class BaseChannelController

@RestController
class ChannelController(
	val channelService: ChannelService,
	val musicSearchService: MusicSearchService
): BaseChannelController() {

	@GetMapping("/{channelId}")
	fun join(
		@PathVariable channelId: String
	): Channel {
		return channelService.join(channelId)
	}

	/**
	 * How about restricting the genre of searchable music by channel?
	 */
	@GetMapping("/search-music/{channelId}")
	fun searchMusic(
		@PathVariable channelId: String,
		@PathVariable userId: String
	): Music {
		return musicSearchService.search(channelId)
	}
}