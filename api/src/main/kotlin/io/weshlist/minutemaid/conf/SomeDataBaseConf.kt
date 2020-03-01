package io.weshlist.minutemaid.conf

import io.weshlist.minutemaid.client.SomeDataBaseClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SomeDataBaseConf {

	@Bean
	fun someDataBaseClient() = SomeDataBaseClient()
}