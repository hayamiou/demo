package com.example.demo.infrastructure.driving.controller.dto

data class BookDTO(
    val id: Long? = null,
    val title: String,
    val author: String,
    val reserved: Boolean = false
)