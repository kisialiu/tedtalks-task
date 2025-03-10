package com.io.uladzimir.tedtalks.model

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.*
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "tedtalks")
data class TedTalk(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    val title: String,
    val author: String,
    val date: LocalDate,
    val views: Int,
    val likes: Int,
    val link: String
)
