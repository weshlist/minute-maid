package io.weshlist.minutemaid.controller

import io.weshlist.minutemaid.result.BaseError
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.service.UserService
import io.weshlist.minutemaid.utils.PrintLog
import io.weshlist.minutemaid.utils.RestApiResponse
import io.weshlist.minutemaid.utils.UserID
import io.weshlist.minutemaid.utils.toResponse
import io.weshlist.minutemaid.utils.validator.MusicValidator
import io.weshlist.minutemaid.utils.validator.UserValidator
import io.weshlist.minutemaid.utils.validator.doValidate
import io.weshlist.minutemaid.utils.validator.onFailure
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@PrintLog
@RestController
@RequestMapping("/user")
class UserController(
	private val userService: UserService
) {
	/**
	 * Request Music from user. Requested Music would be added to channel playlist.
	 * The Playlist of the Channel could be re-calculated when current music's play is done.
	 */
	@PostMapping("/{userId}/music")
	fun requestMusic(
		@PathVariable userId: UserID,
		@RequestBody requestMusicRequest: RequestMusicReq
	): RestApiResponse<Boolean, BaseError> {

		val musicId = requestMusicRequest.musicId

		doValidate(
			UserValidator.checkUserId(userId),
			MusicValidator.checkMusicId(musicId)
		) onFailure { return Result.Failure(it).toResponse() }

		return userService.requestMusic(userId, musicId).toResponse()
	}


}