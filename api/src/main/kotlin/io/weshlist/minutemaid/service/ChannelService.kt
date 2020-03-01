package io.weshlist.minutemaid.service

import io.weshlist.minutemaid.repository.Channel
import io.weshlist.minutemaid.repository.ChannelError
import io.weshlist.minutemaid.repository.ChannelRepository
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.result.onFailure
import io.weshlist.minutemaid.result.onFailureWhen
import io.weshlist.minutemaid.result.toSuccess
import org.springframework.stereotype.Service

@Service
class ChannelService(
	val channelRepository: ChannelRepository
) {

	fun join(channelId: String): Result<Channel, ChannelError> {

		return channelRepository.getChannel(channelId)
			.onFailureWhen(ChannelError.NotFound::class) {
				channelRepository.createChannel(channelId)
			}
			.onFailure {
				return it
			}
			.toSuccess()
	}
}