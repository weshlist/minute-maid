include("api")

pluginManagement {
	val springBootVersion: String by settings
	val kotlinVersion: String by settings

	resolutionStrategy {
		eachPlugin {
			// Plugins Support
			when (requested.id.id) {
				// Kotlin
				"org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
				"org.jetbrains.kotlin.kapt" -> useVersion(kotlinVersion)
				"org.jetbrains.kotlin.plugin.spring" -> useVersion(kotlinVersion)
				"org.jetbrains.kotlin.plugin.allopen" -> useVersion(kotlinVersion)
				"org.jetbrains.kotlin.plugin.serialization" -> useVersion(kotlinVersion)
				"org.jetbrains.kotlin.plugin.noarg" -> useVersion(kotlinVersion)
				"org.jetbrains.kotlin.plugin.jpa" -> useVersion(kotlinVersion)

				// Spring
				"org.springframework.boot" -> useVersion(springBootVersion)
			}
		}
	}

	repositories {
		gradlePluginPortal()
		mavenCentral()
		jcenter()
	}
}