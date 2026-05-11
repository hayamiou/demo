package com.example.demo.usecase

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookRepository

class FakeBookRepository : BookRepository {
    private val books = mutableListOf<Book>()
    private var idCounter = 1L

    override fun save(book: Book) {
        books.add(book.copy(id = idCounter++))
    }

    override fun findAll(): List<Book> = books.toList()

    override fun findById(id: Long): Book? = books.find { it.id == id }

    override fun reserve(id: Long) {
        val index = books.indexOfFirst { it.id == id }
        if (index != -1) {
            books[index] = books[index].copy(reserved = true)
        }
    }
}