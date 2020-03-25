package io.weshlist.minutemaid.controller

import io.weshlist.minutemaid.controller.params.JoinChannelRequest
import io.weshlist.minutemaid.model.Channel
import io.weshlist.minutemaid.model.Music
import io.weshlist.minutemaid.result.BaseError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.result.get
import io.weshlist.minutemaid.result.onFailure
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
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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

	@GetMapping(
		value = ["/{channelId}/m3u8"],
		produces = [MediaType.TEXT_PLAIN_VALUE]
	)
	fun getM3u8(
		@PathVariable channelId: ChannelID
	): ResponseEntity<Resource> {

		val rst = channelService.getM3u8(channelId).onFailure { return ResponseEntity.notFound().build() }

		println(rst)

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, "application/x-mpegURL")
			.header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=sample.m3u8")
			.body(ByteArrayResource(rst.toByteArray()))
	}
}