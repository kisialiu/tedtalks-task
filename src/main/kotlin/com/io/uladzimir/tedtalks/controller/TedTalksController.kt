package com.io.uladzimir.tedtalks.controller

import com.io.uladzimir.tedtalks.service.TedTalksService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/v1/tedtalks")
class TedTalksController(private val tedTalksService: TedTalksService) {

    @PostMapping
    fun bulkUploadFromFile(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        if (file.isEmpty) {
            return ResponseEntity.badRequest().body("File is empty. Please check")
        }

        tedTalksService.importCsv(file)
        return ResponseEntity.ok("CSV file was uploaded to the database successfully!")
    }
}