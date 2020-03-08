package io.weshlist.minutemaid.conf

import io.weshlist.minutemaid.repository.ChannelRepository
import io.weshlist.minutemaid.repository.MusicRepository
import io.weshlist.minutemaid.repository.UserRepository
import io.weshlist.minutemaid.repository.impl.ChannelRepositoryImpl
import io.weshlist.minutemaid.repository.impl.MusicRepositoryImpl
import io.weshlist.minutemaid.repository.impl.UserRepositoryImpl
import io.weshlist.minutemaid.utils.IdGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class ApiConf() {

	@Bean
	fun channelRepository(
		idGenerator: IdGenerator,
		mongoTemplate: MongoTemplate
	): ChannelRepository = ChannelRepositoryImpl(idGenerator, mongoTemplate)

	@Bean
	fun musicRepository(
		idGenerator: IdGenerator,
		mongoTemplate: MongoTemplate
	): MusicRepository = MusicRepositoryImpl(idGenerator, mongoTemplate)

	@Bean
	fun userRepository(
		idGenerator: IdGenerator,
		mongoTemplate: MongoTemplate
	): UserRepository = UserRepositoryImpl(idGenerator, mongoTemplate)
}