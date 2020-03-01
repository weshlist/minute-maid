package io.weshlist.minutemaid.conf

import io.weshlist.minutemaid.client.SomeDataBaseClient
import io.weshlist.minutemaid.repository.ChannelRepository
import io.weshlist.minutemaid.repository.MusicRepository
import io.weshlist.minutemaid.repository.impl.ChannelRepositoryImpl
import io.weshlist.minutemaid.repository.impl.MusicRepositoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApiConf() {

	@Bean
	fun channelRepository(
		someDataBaseClient: SomeDataBaseClient
	): ChannelRepository = ChannelRepositoryImpl(someDataBaseClient)

	@Bean
	fun musicRepository(
		someDataBaseClient: SomeDataBaseClient
	): MusicRepository = MusicRepositoryImpl(someDataBaseClient)
}