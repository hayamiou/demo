package com.example.demo.domain.usecase

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookRepository

class BookUseCase(private val bookRepository: BookRepository) {

    fun addBook(title: String, author: String) {
        require(title.isNotBlank()) { "Title must not be blank" }
        require(author.isNotBlank()) { "Author must not be blank" }
        bookRepository.save(Book(title = title, author = author))
    }

    fun getAllBooks(): List<Book> {
        return bookRepository.findAll().sortedBy { it.title }
    }

    fun reserveBook(id: Long) {
        val book = bookRepository.findById(id)
            ?: throw IllegalArgumentException("Book not found")
        require(!book.reserved) { "Book is already reserved" }
        bookRepository.reserve(id)
    }
}