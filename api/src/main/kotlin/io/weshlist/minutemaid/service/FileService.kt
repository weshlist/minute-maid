package io.weshlist.minutemaid.service

import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.nio.file.Paths
import org.springframework.core.io.Resource
import java.nio.file.Files

@Service
class FileService(
) {
    val rootLocation = Paths.get("api/src/main/kotlin/io/weshlist/minutemaid/sources")

    fun init() {
        Files.createDirectories(rootLocation)
    }


    fun loadFile(fileName: String): Resource {
        val filePath = rootLocation.resolve(fileName)
        val resource = UrlResource(filePath.toUri())

        if (resource.exists() && resource.isReadable) {
            return resource
        } else {
            throw RuntimeException("Invalid resource")
        }
    }
}
