package io.weshlist.minutemaid.conf

import io.weshlist.minutemaid.repository.RoomRepository
import io.weshlist.minutemaid.repository.impl.RoomRepositoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApiConf() {

	@Bean
	fun roomRepository(): RoomRepository = RoomRepositoryImpl()
}