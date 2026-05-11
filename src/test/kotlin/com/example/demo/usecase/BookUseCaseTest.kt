package com.example.demo.usecase

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookRepository
import com.example.demo.domain.usecase.BookUseCase
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.Codepoint
import io.kotest.property.arbitrary.az
import io.kotest.property.arbitrary.string
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.kotest.property.checkAll as kotestCheckAll

class BookUseCaseTest : StringSpec({

    val repository = mockk<BookRepository>()
    val useCase = BookUseCase(repository)
    val fakeRepository = FakeBookRepository()
    val fakeUseCase = BookUseCase(fakeRepository)

    "addBook should save the book" {
        every { repository.save(any()) } returns Unit

        useCase.addBook("Clean Code", "Robert Martin")

        verify(exactly = 1) { repository.save(Book(title = "Clean Code", author = "Robert Martin")) }
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
            Book(title = "Clean Code", author = "Robert Martin"),
            Book(title = "Architecture Hexagonale", author = "Alistair Cockburn"),
            Book(title = "TDD by Example", author = "Kent Beck")
        )
        every { repository.findAll() } returns books

        val result = useCase.getAllBooks()

        result shouldContainExactly listOf(
            Book(title = "Architecture Hexagonale", author = "Alistair Cockburn"),
            Book(title = "Clean Code", author = "Robert Martin"),
            Book(title = "TDD by Example", author = "Kent Beck")
        )
    }

    "property: all saved books are returned" {
        kotestCheckAll(Arb.string(1..20, Codepoint.az()), Arb.string(1..20, Codepoint.az())) { title, author ->
            fakeUseCase.addBook(title, author)
        }

        fakeRepository.findAll().size shouldBe fakeUseCase.getAllBooks().size
    }

    "property: getAllBooks is always sorted alphabetically" {
        kotestCheckAll(Arb.string(1..20, Codepoint.az()), Arb.string(1..20, Codepoint.az())) { title, author ->
            fakeUseCase.addBook(title, author)
        }

        val result = fakeUseCase.getAllBooks()
        result shouldBe result.sortedBy { it.title }
    }

    "property: added book is always in the returned list" {
        kotestCheckAll(Arb.string(1..20, Codepoint.az()), Arb.string(1..20, Codepoint.az())) { title, author ->
            fakeUseCase.addBook(title, author)
            fakeUseCase.getAllBooks().map { it.title } shouldContain title
        }
    }

    "reserveBook should reserve an available book" {
        val localRepo = FakeBookRepository()
        val localUseCase = BookUseCase(localRepo)
        localRepo.save(Book(title = "Clean Code", author = "Robert Martin"))
        val savedBook = localRepo.findAll().first()

        localUseCase.reserveBook(savedBook.id!!)

        localRepo.findById(savedBook.id!!)!!.reserved shouldBe true
    }

    "reserveBook should throw when book is already reserved" {
        val localRepo = FakeBookRepository()
        val localUseCase = BookUseCase(localRepo)
        localRepo.save(Book(title = "Clean Code", author = "Robert Martin", reserved = true))
        val savedBook = localRepo.findAll().first()

        shouldThrow<IllegalArgumentException> {
            localUseCase.reserveBook(savedBook.id!!)
        }.message shouldBe "Book is already reserved"
    }

    "reserveBook should throw when book does not exist" {
        val localRepo = FakeBookRepository()
        val localUseCase = BookUseCase(localRepo)

        shouldThrow<IllegalArgumentException> {
            localUseCase.reserveBook(999L)
        }.message shouldBe "Book not found"
    }
})