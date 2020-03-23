tasks.wrapper { gradleVersion = "6.1" }

subprojects {
	group = "minute-maid"
	version = "1.0.0"

	repositories {
		gradlePluginPortal()
		mavenCentral()

		// Kotlinx
		maven("https://kotlin.bintray.com/kotlinx")
	}
}