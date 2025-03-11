package com.io.uladzimir.tedtalks.service

import com.io.uladzimir.tedtalks.exception.TedTalkFoundException
import com.io.uladzimir.tedtalks.exception.TedTalkNotFoundException
import com.io.uladzimir.tedtalks.model.Influencer
import com.io.uladzimir.tedtalks.model.TedTalk
import com.io.uladzimir.tedtalks.repository.TedTalksRepository
import com.io.uladzimir.tedtalks.util.DateUtil
import com.opencsv.CSVReader
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import kotlin.math.log10

private val logger = KotlinLogging.logger {}

@Service
class TedTalksService(private val tedTalksRepository: TedTalksRepository) {

    fun saveTedTalk(tedTalk: TedTalk): TedTalk {
        logger.info { "Saving tedTalk: $tedTalk" }
        if (tedTalksRepository.existsTedTalkByTitleAndAuthor(tedTalk.title, tedTalk.author)) {
             throw TedTalkFoundException("TedTalk with the same title and author already exists")
        }
        return tedTalksRepository.save(tedTalk)
    }

    fun findByTitle(title: String): TedTalk {
        logger.info { "Finding a tedTalk by title '$title'" }
        val found = tedTalksRepository.findByTitle(title)
        return found ?: throw TedTalkNotFoundException("TedTalk with title '$title' not found")
    }

    fun findByAuthor(author: String): List<TedTalk> {
        logger.info { "Finding a tedTalk by author '$author'" }
        val found = tedTalksRepository.findByAuthor(author)
        return found.ifEmpty { throw TedTalkNotFoundException("TedTalks with author '$author' not found") }
    }

    fun findByTitleAndAuthor(title: String, author: String): List<TedTalk> {
        logger.info { "Finding a tedTalk by title '$title' and author '$author'" }
        val found = tedTalksRepository.findByTitleAndAuthor(title, author)
        return found.ifEmpty { throw TedTalkNotFoundException("TedTalks with title '$title' and author '$author' not found") }
    }

    fun importCsv(file: MultipartFile) {
        logger.info { "Bulk upload tedTalks to the database" }
        file.inputStream.use { input ->
            val csvReader = CSVReader(InputStreamReader(input, StandardCharsets.UTF_8))
            val tedTalks: List<TedTalk> = csvReader
                .readAll()
                .drop(1)
                .mapNotNull { mapToTedTalk(it) }

            val existing = getExisting(tedTalks).map { it.title }.toSet()
            val newTalks = tedTalks.filter { it.title !in existing }

            if (newTalks.isNotEmpty()) {
                tedTalksRepository.saveAll(tedTalks)
            }
        }
    }

    fun getSpeakersByInfluence(n: Int): List<Influencer> {
        logger.info { "Getting top $n influencers" }
        val allTalks = tedTalksRepository.findAll()
        return allTalks.groupBy { it.author }
            .mapValues { (_, talks) -> talks.sumOf { calculateInfluence(it) } }
            .entries
            .sortedByDescending { it.value }
            .take(n)
            .map { Influencer(it.key, it.value) }
    }

    private fun calculateInfluence(tedTalk: TedTalk): Double {
        val viewsFactor = if (tedTalk.views > 0) log10(tedTalk.views.toDouble() + 1) else 0.0
        val likesFactor = if (tedTalk.likes > 0) log10(tedTalk.likes.toDouble() + 1) else 0.0
        val rate = if (tedTalk.views > 0) tedTalk.likes.toDouble() / tedTalk.views else 0.0

        return viewsFactor * 0.5 + likesFactor * 0.3 + rate * 100
    }

    private fun getExisting(tedTalks: List<TedTalk>): List<TedTalk> {
        return tedTalksRepository.findAllByTitleIn(tedTalks.map { it.title })
    }

    private fun mapToTedTalk(row: Array<String>): TedTalk? {
        try {
            val title = row[0]
            val author = row[1]
            val date = DateUtil.monthYearToLocalDate(row[2])
            val views = row[3].toIntOrNull() ?: throw IllegalArgumentException("Invalid views: ${row[3]}")
            val likes = row[4].toIntOrNull() ?: throw IllegalArgumentException("Invalid likes: ${row[4]}")
            val link = row[5]

            if (author.isEmpty()) {
                throw IllegalArgumentException("Author can't be empty")
            }

            if (views < 0) {
                throw IllegalArgumentException("Invalid views: ${row[3]}")
            }
            if (likes < 0) {
                throw IllegalArgumentException("Invalid likes: ${row[4]}")
            }

            return TedTalk(
                title = title,
                author = author,
                date = date,
                views = views,
                likes = likes,
                link = link
            )
        } catch (exception: IllegalArgumentException) {
            logger.warn { "There was an exception mapping CSV row to TedTalk: ${exception.message}. Skipping the row." }
            return null
        }

    }

}