package io.weshlist.minutemaid.conf

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

const val INDEX_URL_PATTERN = "/**"

@Configuration
class WebMvcConfiguration : WebMvcConfigurer {
    private val allowOrigin = "http://localhost:3000"

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping(INDEX_URL_PATTERN)
                .allowedOrigins(allowOrigin)
    }
}