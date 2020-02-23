package io.weshlist.minutemaid.repository

data class Room(
	val name: String
)

interface RoomRepository {
	fun getRoom(id: Int): Room?
	fun createRoom(id: Int): Room
}