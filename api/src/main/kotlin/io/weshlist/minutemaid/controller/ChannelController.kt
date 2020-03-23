package io.weshlist.minutemaid.controller

import io.weshlist.minutemaid.model.Channel
import io.weshlist.minutemaid.model.M3u8
import io.weshlist.minutemaid.model.Music
import io.weshlist.minutemaid.result.BaseError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.service.ChannelService
import io.weshlist.minutemaid.service.MusicSearchService
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.PrintLog
import io.weshlist.minutemaid.utils.RestApiResponse
import io.weshlist.minutemaid.utils.UserID
import io.weshlist.minutemaid.utils.toResponse
import io.weshlist.minutemaid.utils.validator.ChannelValidator
import io.weshlist.minutemaid.utils.validator.doValidate
import io.weshlist.minutemaid.utils.validator.onFailure
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
	private val musicSearchService: MusicSearchService
) {

	@PostMapping("/{channelName}/join")
	fun join(
		@PathVariable channelName: String,
		@RequestBody joinChannelRequest: JoinChannelRequest
	): RestApiResponse<Channel, BaseError> {
		// TODO: ID Generate
		val userId = joinChannelRequest.userId

		doValidate(
			ChannelValidator.checkChannelName(channelName),
			ChannelValidator.checkUserId(userId)
		) onFailure { return Result.Failure(it).toResponse() }

		return channelService.join(userId, channelName).toResponse()
	}

	@GetMapping("/{channelId}/quit")
	fun quit(
		@PathVariable channelId: ChannelID,
		@RequestParam userId: UserID
	): RestApiResponse<Boolean, BaseError> {
		// TODO: ID Generate

		doValidate(
			ChannelValidator.checkChannelId(channelId),
			ChannelValidator.checkUserId(userId)
		) onFailure { return Result.Failure(it).toResponse() }

		return channelService.quit(userId, channelId).toResponse()
	}


	/**
	 * How about restricting the genre of searchable music by channel?
	 */
	@GetMapping("/{channelId}/music/search")
	fun searchMusic(
		@PathVariable channelId: ChannelID,
		@PathVariable userId: UserID,

		// Need to consider paging.
		@RequestParam keyword: String? = null
	): RestApiResponse<List<Music>, BaseError> {

		doValidate(
			ChannelValidator.checkChannelId(channelId),
			ChannelValidator.checkUserId(userId)
		) onFailure { return Result.Failure(it).toResponse() }

		val searchResult = keyword
			?.let { searchKeyword -> musicSearchService.search(channelId, searchKeyword) }
			?: musicSearchService.searchAll(channelId)

		return searchResult.toResponse()
	}

	@GetMapping("/{channelId}/playlist")
	fun getPlaylist(
		@PathVariable channelId: ChannelID
	): RestApiResponse<List<Music>, BaseError> {

		doValidate(
			ChannelValidator.checkChannelId(channelId)
		) onFailure { return Result.Failure(it).toResponse() }

		return channelService.getPlaylist(channelId).toResponse()
	}

	@GetMapping("/{channelId}/m3u8")
	fun getM3u8(
		@PathVariable channelId: ChannelID
	): RestApiResponse<M3u8, BaseError> {

		doValidate(
				ChannelValidator.checkChannelId(channelId)
		) onFailure { return Result.Failure(it).toResponse() }

		return channelService.getM3u8(channelId).toResponse()
	}
}