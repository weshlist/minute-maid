package io.weshlist.minutemaid.service

import io.weshlist.minutemaid.repository.Channel
import io.weshlist.minutemaid.repository.ChannelError
import io.weshlist.minutemaid.repository.ChannelRepository
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.result.onFailure
import io.weshlist.minutemaid.result.recover
import org.springframework.stereotype.Service

@Service
class ChannelService(
	val channelRepository: ChannelRepository
) {

	fun join(channelId: String): Result<Channel, ChannelError> {

		val channel = channelRepository.getChannel(channelId)
			.recover { when(it) {
				is ChannelError.NotFound -> Channel("")
				else -> Channel("")
			}
		//.onFailure { return it }
	}
}