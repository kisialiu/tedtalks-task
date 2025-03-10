package com.io.uladzimir.tedtalks.repository

import com.io.uladzimir.tedtalks.model.TedTalk
import org.springframework.data.jpa.repository.JpaRepository

interface TedTalksRepository : JpaRepository<TedTalk, String>