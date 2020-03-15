package io.weshlist.minutemaid.controller

import io.weshlist.minutemaid.service.FileService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.core.io.Resource

@RestController
@RequestMapping("/download")
class FileController (
        private val fileService: FileService
) {
    @GetMapping("/{fileName}")
    fun downloadFile(
            @PathVariable fileName: String
    ): ResponseEntity<Resource> {
        val file = fileService.loadFile(fileName)
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "test filename=\"" + fileName + "\"")
                .body(file)
    }
}
