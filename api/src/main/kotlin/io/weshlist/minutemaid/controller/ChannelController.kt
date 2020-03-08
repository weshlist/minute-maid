package io.weshlist.minutemaid.controller

import io.weshlist.minutemaid.repository.Channel
import io.weshlist.minutemaid.repository.Music
import io.weshlist.minutemaid.result.BaseError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.service.ChannelService
import io.weshlist.minutemaid.service.MusicSearchService
import io.weshlist.minutemaid.service.MusicService
import io.weshlist.minutemaid.service.UserService
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.PrintLog
import io.weshlist.minutemaid.utils.RestApiResponse
import io.weshlist.minutemaid.utils.UserID
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
	private val musicSearchService: MusicSearchService,
	private val userService: UserService
) {

	@GetMapping("/{channelName}/join")
	fun join(
		@PathVariable channelName: String,
		@RequestParam userId: UserID
	): RestApiResponse<Channel, BaseError> {
		// TODO: ID Generate

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

	/**
	 * Request Music from user. Requested Music would be added to channel playlist.
	 */
	@PostMapping("/{channelId}/music")
	fun requestMusic(
		@PathVariable channelId: ChannelID,
		@RequestBody requestMusicRequest: RequestMusicReq
	): RestApiResponse<Boolean, BaseError> {

		val userId = requestMusicRequest.userId
		val musicId = requestMusicRequest.musicId

		doValidate(
			ChannelValidator.checkChannelId(channelId),
			MusicValidator.checkMusicId(musicId)
		) onFailure { return Result.Failure(it).toResponse() }

		return userService.addMusicToPlaylist(userId, channelId).toResponse()
	}
}