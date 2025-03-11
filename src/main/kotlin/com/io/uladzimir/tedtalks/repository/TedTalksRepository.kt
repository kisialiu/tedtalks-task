package com.io.uladzimir.tedtalks.repository

import com.io.uladzimir.tedtalks.model.TedTalk
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TedTalksRepository : JpaRepository<TedTalk, String> {
    fun findAllByTitleIn(titles: List<String>): List<TedTalk>
    fun findByAuthor(author: String): List<TedTalk>
    fun existsTedTalkByTitleAndAuthor(title: String, author: String): Boolean
    fun findByTitle(title: String): TedTalk?
    fun findByTitleAndAuthor(title: String, author: String): List<TedTalk>
    fun findById(id: UUID): TedTalk?
}