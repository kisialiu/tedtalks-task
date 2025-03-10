package com.io.uladzimir.tedtalks.service

import com.io.uladzimir.tedtalks.model.TedTalk
import com.io.uladzimir.tedtalks.repository.TedTalksRepository
import com.io.uladzimir.tedtalks.util.DateUtil
import com.opencsv.CSVReader
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

@Service
class TedTalksService(private val tedTalksRepository: TedTalksRepository) {

    fun importCsv(file: MultipartFile) {
        file.inputStream.use { input ->
            val csvReader = CSVReader(InputStreamReader(input, StandardCharsets.UTF_8))
            val tedTalks: List<TedTalk> = csvReader
                .readAll()
                .drop(1)
                .mapNotNull { mapToTedTalk(it) }

            tedTalksRepository.saveAll(tedTalks)
        }
    }

    private fun mapToTedTalk(row: Array<String>): TedTalk? {
        try {
            val title = row[0]
            val author = row[1]
            val date = DateUtil.monthYearToLocalDate(row[2])
            val views = row[3].toIntOrNull() ?: throw IllegalArgumentException("Invalid views: ${row[3]}")
            val likes = row[4].toIntOrNull() ?: throw IllegalArgumentException("Invalid likes: ${row[4]}")
            val link = row[5]

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
            return null
        }

    }

}