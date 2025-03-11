package com.io.uladzimir.tedtalks.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.io.uladzimir.tedtalks.TestSecurityConfig
import com.io.uladzimir.tedtalks.model.TedTalk
import com.io.uladzimir.tedtalks.service.TedTalksService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.LocalDate
import java.util.*

@WebMvcTest(TedTalksController::class)
@ActiveProfiles("test")
@Import(TestSecurityConfig::class)
class TedTalksControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var tedTalksService: TedTalksService

    private val objectMapper: ObjectMapper = ObjectMapper()

    @Test
    fun `test save ted talk`() {
        objectMapper.registerModule(JavaTimeModule())
        val request = mapOf(
            "title" to "Test Title",
            "author" to "Test Author",
            "date" to "January 2021",
            "views" to 10,
            "likes" to 10,
            "link" to "https://ted.com/talks/test_link"
        )

        val response = TedTalk(
            id = UUID.randomUUID(),
            title = request["title"].toString(),
            author = request["author"].toString(),
            date = LocalDate.of(2021, 1, 1),
            views = 10,
            likes = 10,
            link = request["link"].toString()
        )

        given { tedTalksService.saveTedTalk(any()) }.willReturn(response)

        mockMvc.post("/v1/tedtalks") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            jsonPath("$.id") { exists() }
            jsonPath("$.title") { value(request["title"]) }
            jsonPath("$.author") { value(request["author"]) }
            jsonPath("$.date") { value("2021-01-01") }
            jsonPath("$.views") { value(request["views"]) }
            jsonPath("$.likes") { value(request["likes"]) }
            jsonPath("$.link") { value(request["link"]) }
        }
    }
    // TODO: More tests
}