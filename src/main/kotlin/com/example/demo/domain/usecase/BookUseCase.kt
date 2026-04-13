package com.example.demo.domain.usecase

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookRepository

class BookUseCase(private val bookRepository: BookRepository) {

    fun addBook(title: String, author: String): Book {
        require(title.isNotBlank()) { "Title must not be blank" }
        require(author.isNotBlank()) { "Author must not be blank" }

        val book = Book(title, author)
        bookRepository.save(book)
        return book
    }

    fun getAllBooks(): List<Book> {
        return bookRepository.findAll().sortedBy { it.title }
    }
}