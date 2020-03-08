package io.weshlist.minutemaid.controller

import io.weshlist.minutemaid.result.BaseError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.service.MusicService
import io.weshlist.minutemaid.utils.PrintLog
import io.weshlist.minutemaid.utils.RestApiResponse
import io.weshlist.minutemaid.utils.toResponse
import io.weshlist.minutemaid.utils.validator.MusicValidator
import io.weshlist.minutemaid.utils.validator.doValidate
import io.weshlist.minutemaid.utils.validator.onFailure
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@PrintLog
@RestController
@RequestMapping("/music")
class MusicController(
	private val musicService: MusicService
) {
	@PostMapping("/add")
	fun addMusic(
		@RequestBody addMusicRequest: AddMusicReq
	): RestApiResponse<Boolean, BaseError> {

		val musicMeta = addMusicRequest.musicMeta

		doValidate(
			MusicValidator.checkMusicTitle(musicMeta.title)
		) onFailure { return Result.Failure(it).toResponse() }

		return musicService.add(musicMeta).toResponse()
	}


}