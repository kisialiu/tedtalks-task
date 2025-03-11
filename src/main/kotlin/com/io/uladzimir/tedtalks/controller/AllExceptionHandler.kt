package com.io.uladzimir.tedtalks.controller

import com.io.uladzimir.tedtalks.exception.TedTalkFoundException
import com.io.uladzimir.tedtalks.exception.TedTalkNotFoundException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AllExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleGeneral(ex: Exception): ResponseEntity<Map<String, String>> {
        val message = ex.message ?: "Exception was raised"

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to message))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleMethodArgumentException(ex: ConstraintViolationException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to ex.toString()))
    }

    @ExceptionHandler(TedTalkFoundException::class)
    fun handleTedTalkFound(ex: TedTalkFoundException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to ex.message))
    }

    @ExceptionHandler(TedTalkNotFoundException::class)
    fun handleTedTalkNotFound(ex: TedTalkNotFoundException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("message" to ex.message))
    }
}
