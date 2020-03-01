package io.weshlist.minutemaid.repository.impl

import io.weshlist.minutemaid.repository.Channel
import io.weshlist.minutemaid.repository.ChannelError
import io.weshlist.minutemaid.repository.ChannelRepository
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.result.Result.Success
import io.weshlist.minutemaid.result.Result.Failure
import io.weshlist.minutemaid.result.onFailure
import io.weshlist.minutemaid.result.resultFrom

class ChannelRepositoryImpl(
	private val someDataBaseClient: SomeDataBaseClient,
	private val someOtherDataBaseClient: SomeOtherDataBaseClient
) : ChannelRepository {

	override fun getChannel(id: String): Result<Channel, ChannelError> {
		//todo: will be change
		if (id == "NOT_FOUND") {
			return Failure(ChannelError.NotFound(id))
		}

		if (id == "MALFORMED") {
			return Failure(ChannelError.MalformedName(id))
		}

		val result1 = resultFrom { someDataBaseClient.get(id) }
			.onFailure {
				return Failure(ChannelError.DatabaseError(id, it.reason))
			}

		val result2 = resultFrom { someOtherDataBaseClient.get(id) }
			.onFailure {
				return Failure(ChannelError.OtherDatabaseError(id, it.reason))
			}

		return Success(Channel("Welcome Channel $result1"))
	}

	override fun createChannel(id: String): Result<Channel, ChannelError> {
		return Success(Channel("Create Channel"))
	}
}

class SomeDataBaseClient {
	fun get(id: String): String {
		if (id == "EXCEPTION") {
			throw Exception("Some Exception Occurred")
		} else {
			return id
		}
	}
}

class SomeOtherDataBaseClient {
	fun get(id: String): String {
		if (id == "EXCEPTION") {
			throw Exception("Some Exception Occurred")
		} else {
			return id
		}
	}
}