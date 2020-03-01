package io.weshlist.minutemaid.conf

import io.weshlist.minutemaid.repository.ChannelRepository
import io.weshlist.minutemaid.repository.impl.ChannelRepositoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApiConf() {

	@Bean
	fun roomRepository(): ChannelRepository = ChannelRepositoryImpl()
}