val springBootVersion: String by project
val kotlinLoggingVersion: String by project

plugins {
	kotlin("jvm")
	kotlin("kapt")
	kotlin("plugin.spring")
	kotlin("plugin.allopen")
	kotlin("plugin.serialization")
	id("org.springframework.boot")
	id("idea")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))

	api("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.+")
	api("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
	api("org.springframework.boot:spring-boot-starter-aop:$springBootVersion")
	api("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")

}

tasks {
	compileKotlin {
		kotlinOptions.jvmTarget = "1.8"
	}
	compileTestKotlin {
		kotlinOptions.jvmTarget = "1.8"
	}
}