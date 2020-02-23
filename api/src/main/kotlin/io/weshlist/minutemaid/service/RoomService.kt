package io.weshlist.minutemaid.service

import io.weshlist.minutemaid.repository.Room
import io.weshlist.minutemaid.repository.RoomRepository
import org.springframework.stereotype.Service

@Service
class RoomService(
	val roomRepository: RoomRepository
) {

	fun join(id: Int): Room {
		return roomRepository.getRoom(id)
			?: roomRepository.createRoom(id)
	}
}