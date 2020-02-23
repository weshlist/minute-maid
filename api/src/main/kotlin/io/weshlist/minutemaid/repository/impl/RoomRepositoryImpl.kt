package io.weshlist.minutemaid.repository.impl

import io.weshlist.minutemaid.repository.Room
import io.weshlist.minutemaid.repository.RoomRepository

class RoomRepositoryImpl(): RoomRepository {

	override fun getRoom(id: Int): Room? {
		//todo: will be change
		if(id % 2 == 0) {
			return null
		}
		return Room("Welcome Room")
	}

	override fun createRoom(id: Int): Room {
		return Room("Create Room")
	}
}