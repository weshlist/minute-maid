package io.weshlist.minutemaid.controller

import io.weshlist.minutemaid.repository.Room
import io.weshlist.minutemaid.service.RoomService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class RoomController(
	val roomService: RoomService
) {

	@GetMapping("/{id}")
	fun join(@PathVariable id: Int): Room {
		return roomService.join(id)
	}
}