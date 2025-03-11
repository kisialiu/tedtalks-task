package com.io.uladzimir.tedtalks.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.io.uladzimir.tedtalks.config.MonthYearDeserializer
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.PositiveOrZero
import org.hibernate.validator.constraints.URL
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "tedtalks")
data class TedTalk(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    @field:NotEmpty(message = "Title must not be empty")
    val title: String,
    @field:NotEmpty(message = "Author must not be empty")
    val author: String,
    @JsonDeserialize(using = MonthYearDeserializer::class)
    @field:PastOrPresent(message = "The date should be in the current month or in the past")
    val date: LocalDate,
    @field:PositiveOrZero(message = "Views count must be greater or equal to zero")
    val views: Int,
    @field:PositiveOrZero(message = "Likes count must be greater or equal to zero")
    val likes: Int,
    @field:NotEmpty(message = "Link must not be empty")
    @field:URL(message = "Link should be a valid URL")
    val link: String
)
