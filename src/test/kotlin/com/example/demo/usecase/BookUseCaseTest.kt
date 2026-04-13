package com.example.demo.usecase

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookRepository
import com.example.demo.domain.usecase.BookUseCase
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class BookUseCaseTest : StringSpec({

    val repository = mockk<BookRepository>()
    val useCase = BookUseCase(repository)

    // Tests classiques
    "addBook should return the created book" {
        every { repository.save(any()) } returns Unit

        val book = useCase.addBook("Clean Code", "Robert Martin")

        book.title shouldBe "Clean Code"
        book.author shouldBe "Robert Martin"
        verify(exactly = 1) { repository.save(book) }
    }

    "addBook with blank title should throw IllegalArgumentException" {
        shouldThrow<IllegalArgumentException> {
            useCase.addBook("", "Robert Martin")
        }
    }

    "addBook with blank author should throw IllegalArgumentException" {
        shouldThrow<IllegalArgumentException> {
            useCase.addBook("Clean Code", "")
        }
    }

    "getAllBooks should return books sorted alphabetically by title" {
        val books = listOf(
            Book("Clean Code", "Robert Martin"),
            Book("Architecture Hexagonale", "Alistair Cockburn"),
            Book("TDD by Example", "Kent Beck")
        )
        every { repository.findAll() } returns books

        val result = useCase.getAllBooks()

        result shouldContainExactly listOf(
            Book("Architecture Hexagonale", "Alistair Cockburn"),
            Book("Clean Code", "Robert Martin"),
            Book("TDD by Example", "Kent Beck")
        )
    }
})