package io.weshlist.minutemaid.utils

import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

// TODO: Id generator with database
@Component
class IdGenerator {
	private val channelSeq = AtomicLong(-1L)
	private val musicSeq = AtomicLong(-1L)
	private val userSeq = AtomicLong(-1L)

	fun generateChannelId(): String {
		val newSeq = channelSeq.incrementAndGet()

		return "channel$newSeq"
	}

	fun generateMusicId(): String {
		val newSeq = musicSeq.incrementAndGet()

		return "music$newSeq"
	}

	fun generateUserId(): String {
		val newSeq = userSeq.incrementAndGet()

		return "user$newSeq"
	}


}