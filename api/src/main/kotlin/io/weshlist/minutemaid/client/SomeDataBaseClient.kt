package io.weshlist.minutemaid.client

class SomeDataBaseClient {
	fun get(id: String): String {
		if (id == "EXCEPTION") {
			throw Exception("Some Exception Occurred")
		} else {
			return id
		}
	}
}