package com.example.demo

import com.example.demo.domain.model.Book
import com.example.demo.domain.usecase.BookUseCase
import com.example.demo.infrastructure.driving.controller.BookController
import com.example.demo.infrastructure.driving.controller.GlobalExceptionHandler
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(BookController::class, GlobalExceptionHandler::class)
class BookControllerIT(val mockMvc: MockMvc) : FunSpec() {

    override fun extensions() = listOf(SpringExtension)

    @MockkBean
    lateinit var bookUseCase: BookUseCase

    init {
        test("GET /books should return list of books sorted alphabetically") {
            every { bookUseCase.getAllBooks() } returns listOf(
                Book("Clean Code", "Robert Martin"),
                Book("TDD by Example", "Kent Beck")
            )

            mockMvc.get("/books")
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        json("""
                            [
                                {"title": "Clean Code", "author": "Robert Martin"},
                                {"title": "TDD by Example", "author": "Kent Beck"}
                            ]
                        """.trimIndent())
                    }
                }

            verify(exactly = 1) { bookUseCase.getAllBooks() }
        }

        test("POST /books should create a book and return 201") {
            every { bookUseCase.addBook(any(), any()) } returns Book("Clean Code", "Robert Martin")

            mockMvc.post("/books") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"title": "Clean Code", "author": "Robert Martin"}"""
            }.andExpect {
                status { isCreated() }
            }

            verify(exactly = 1) { bookUseCase.addBook("Clean Code", "Robert Martin") }
        }

        test("POST /books with blank title should return 400") {
            every { bookUseCase.addBook("", "Robert Martin") } throws IllegalArgumentException("Title must not be blank")

            mockMvc.post("/books") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"title": "", "author": "Robert Martin"}"""
            }.andExpect {
                status { isBadRequest() }
            }
        }

        test("POST /books with blank author should return 400") {
            every { bookUseCase.addBook("Clean Code", "") } throws IllegalArgumentException("Author must not be blank")

            mockMvc.post("/books") {
                contentType = MediaType.APPLICATION_JSON
                content = """{"title": "Clean Code", "author": ""}"""
            }.andExpect {
                status { isBadRequest() }
            }
        }
    }
}