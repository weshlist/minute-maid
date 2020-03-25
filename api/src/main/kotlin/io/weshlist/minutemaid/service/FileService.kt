package io.weshlist.minutemaid.service

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.nio.file.Paths
import org.springframework.core.io.Resource
import java.nio.file.Files

@Service
class FileService(
) {
    fun loadFile(fileName: String): Resource {
        val filePath = Paths.get(ClassPathResource("sources/$fileName.ts").uri)
        val resource = UrlResource(filePath.toUri())

        if (resource.exists() && resource.isReadable) {
            return resource
        } else {
            throw RuntimeException("Invalid resource")
        }
    }
}
