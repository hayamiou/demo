package com.example.demo.infrastructure.driving.controller

import com.example.demo.domain.usecase.BookUseCase
import com.example.demo.infrastructure.driving.controller.dto.BookDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(private val bookUseCase: BookUseCase) {

    @GetMapping
    fun getAllBooks(): List<BookDTO> {
        return bookUseCase.getAllBooks()
            .map { BookDTO(it.id, it.title, it.author, it.reserved) }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBook(@RequestBody bookDTO: BookDTO) {
        bookUseCase.addBook(bookDTO.title, bookDTO.author)
    }

    @PatchMapping("/{id}/reserve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun reserveBook(@PathVariable id: Long) {
        bookUseCase.reserveBook(id)
    }
}