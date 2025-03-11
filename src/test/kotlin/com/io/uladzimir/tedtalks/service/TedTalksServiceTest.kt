package com.io.uladzimir.tedtalks.service

import com.io.uladzimir.tedtalks.model.TedTalk
import com.io.uladzimir.tedtalks.repository.TedTalksRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TedTalksServiceTest(
    @Autowired private val tedTalksService: TedTalksService,
    @Autowired private val tedTalksRepository: TedTalksRepository
) {

    @Test
    fun `test create a talk`() {
        val request = TedTalk(
            title = "Test Title",
            author = "Test Author",
            date = LocalDate.now(),
            views = 10,
            likes = 10,
            link = "https://ted.com/talks/test_link"
        )

        val response = tedTalksService.saveTedTalk(request)
        val fromDb = tedTalksRepository.findById(response.id!!)

        assertNotNull(response)
        assertNotNull(response.id)
        assertEquals(request.copy(id = response.id), response)
        assertEquals(request.copy(id = response.id), fromDb)
    }
    // TODO: More tests!!!

}