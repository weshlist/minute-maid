package io.weshlist.minutemaid.conf

import com.mongodb.MongoClient
import de.flapdoodle.embed.mongo.Command
import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodProcess
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.IMongodConfig
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.config.io.ProcessOutput
import de.flapdoodle.embed.process.io.Processors
import de.flapdoodle.embed.process.io.Slf4jLevel
import de.flapdoodle.embed.process.runtime.Network
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import java.io.IOException


@Configuration
class EmbeddedMongoConfig {

	private val bindIp = "127.0.0.1"
	private val port = 27017
	private val database = "weshlist"

	@Bean
	fun iMongodConfig(): IMongodConfig {
		return MongodConfigBuilder()
			.version(Version.Main.V4_0)
			.net(Net(bindIp, port, Network.localhostIsIPv6()))
			.build()
	}

	@Bean
	fun mongoClient(): MongoClient {
		return MongoClient(bindIp, port)
	}

	@Bean(destroyMethod = "stop")
	@Throws(IOException::class)
	fun mongodExecutable(mongodStarter: MongodStarter, iMongodConfig: IMongodConfig): MongodExecutable? {
		return mongodStarter.prepare(iMongodConfig)
	}

	@Bean
	fun mongodStarter(): MongodStarter? {
		val log = KotlinLogging.logger {}

		val processOutput = ProcessOutput(
			Processors.logTo(log, Slf4jLevel.DEBUG),
			Processors.logTo(log, Slf4jLevel.ERROR),
			Processors.logTo(log, Slf4jLevel.DEBUG)
		)

		val runtimeConfig = RuntimeConfigBuilder()
			.defaultsWithLogger(Command.MongoD, log)
			.processOutput(processOutput)
			.build()

		return MongodStarter.getInstance(runtimeConfig)
	}

	@Bean(destroyMethod = "stop")
	@Throws(IOException::class)
	fun mongodProcess(mongodExecutable: MongodExecutable): MongodProcess? {
		return mongodExecutable.start()
	}

	@Bean
	fun mongoTemplate(
		mongoClient: MongoClient
	): MongoTemplate {
		return MongoTemplate(mongoClient, database)
	}
}