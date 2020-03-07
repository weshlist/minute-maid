package io.weshlist.minutemaid.controller

import io.weshlist.minutemaid.repository.Channel
import io.weshlist.minutemaid.repository.Music
import io.weshlist.minutemaid.result.BaseError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.service.ChannelService
import io.weshlist.minutemaid.service.MusicSearchService
import io.weshlist.minutemaid.service.MusicService
import io.weshlist.minutemaid.utils.PrintLog
import io.weshlist.minutemaid.utils.RestApiResponse
import io.weshlist.minutemaid.utils.toResponse
import io.weshlist.minutemaid.utils.validator.ChannelValidator
import io.weshlist.minutemaid.utils.validator.MusicValidator
import io.weshlist.minutemaid.utils.validator.doValidate
import io.weshlist.minutemaid.utils.validator.onFailure
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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

@PrintLog
@RestController
@RequestMapping("/channel")
class ChannelController(
	private val channelService: ChannelService,
	private val musicService: MusicService,
	private val musicSearchService: MusicSearchService
) {

	@GetMapping("/join/{channelId}")
	fun join(
		@PathVariable channelId: String
	): RestApiResponse<Channel, BaseError> {

		doValidate(
			ChannelValidator.checkChannelId(channelId)
		) onFailure { return Result.Failure(it).toResponse() }

		return channelService.join(channelId).toResponse()
	}

	/**
	 * How about restricting the genre of searchable music by channel?
	 */
	@GetMapping("/music/search/{channelId}")
	fun searchMusic(
		@PathVariable channelId: String,
		@PathVariable userId: String
	): RestApiResponse<List<Music>, BaseError> {

		doValidate(
			ChannelValidator.checkChannelId(channelId),
			ChannelValidator.checkUserId(userId)
		) onFailure { return Result.Failure(it).toResponse() }

		return musicSearchService.search(channelId).toResponse()
	}

	@PostMapping("/music/add")
	fun addMusic(
		@RequestBody addMusicRequest: AddMusicRequest
	): RestApiResponse<Boolean, BaseError> {

		val channelId = addMusicRequest.channelId
		val musicId = addMusicRequest.musicId

		doValidate(
			ChannelValidator.checkChannelId(channelId),
			MusicValidator.checkMusicId(musicId)
		) onFailure { return Result.Failure(it).toResponse() }

		return musicService.add(musicId, channelId).toResponse()
	}
}