package io.weshlist.minutemaid.service

import io.weshlist.minutemaid.repository.Channel
import io.weshlist.minutemaid.repository.ChannelError
import io.weshlist.minutemaid.repository.ChannelRepository
import io.weshlist.minutemaid.result.Result
import io.weshlist.minutemaid.result.onFailure
import io.weshlist.minutemaid.result.onFailureWhen
import io.weshlist.minutemaid.result.toSuccess
import io.weshlist.minutemaid.utils.ChannelID
import io.weshlist.minutemaid.utils.UserID
import org.springframework.stereotype.Service

@Service
class ChannelService(
	private val channelRepository: ChannelRepository
) {

	fun join(userId: UserID, channelName: String): Result<Channel, ChannelError> {

		return channelRepository.getChannel(channelName)
			.onFailureWhen(ChannelError.NotFound::class) {
				channelRepository.createChannel(userId, channelName)
			}
			.onFailure {
				return it
			}
			.toSuccess()
	}

	fun quit(userId: UserID, channelId: ChannelID): Result<Boolean, ChannelError> {
		return channelRepository.quitChannel(userId, channelId)
	}

}