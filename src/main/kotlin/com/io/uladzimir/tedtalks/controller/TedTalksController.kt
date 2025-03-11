package com.io.uladzimir.tedtalks.controller

import com.io.uladzimir.tedtalks.model.Influencer
import com.io.uladzimir.tedtalks.model.TedTalk
import com.io.uladzimir.tedtalks.service.TedTalksService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/v1/tedtalks")
class TedTalksController(private val tedTalksService: TedTalksService) {

    @PostMapping("/upload")
    fun bulkUploadFromFile(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        if (file.isEmpty) {
            return ResponseEntity.badRequest().body("File is empty. Please check")
        }

        tedTalksService.importCsv(file)
        return ResponseEntity.ok("CSV file was uploaded to the database successfully!")
    }

    @PostMapping
    fun saveTedTalk(@RequestBody tedTalk: TedTalk): ResponseEntity<TedTalk> {
        val talk = tedTalksService.saveTedTalk(tedTalk)

        return ResponseEntity(talk, HttpStatus.CREATED)
    }

    @GetMapping
    fun getTedTalksByTitleAndAuthor(
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) author: String?
    ): ResponseEntity<List<TedTalk>> {
        val talks = when {
            title != null && author != null -> {
                tedTalksService.findByTitleAndAuthor(title, author)
            }
            title != null -> {
                listOf(tedTalksService.findByTitle(title))
            }
            author != null -> {
                tedTalksService.findByAuthor(author)
            }
            else -> {
                emptyList()
            }
        }

        return ResponseEntity.ok(talks)
    }

    @GetMapping("/top-influencers")
    fun getTopNInfluencers(@RequestParam(defaultValue = "10") topNumber: Int): ResponseEntity<List<Influencer>> {
        val top = tedTalksService.getSpeakersByInfluence(topNumber)

        return ResponseEntity.ok(top)
    }
}