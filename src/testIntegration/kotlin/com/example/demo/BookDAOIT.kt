package com.example.demo

import com.example.demo.domain.model.Book
import com.example.demo.infrastructure.driven.BookDAO
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@ActiveProfiles("testIntegration")
@Testcontainers
class BookDAOIT(
    private val bookDAO: BookDAO,
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) : FunSpec() {

    override fun extensions() = listOf(SpringExtension)

    companion object {
        @Container
        val container = PostgreSQLContainer<Nothing>("postgres:13-alpine").apply {
            start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.username", container::getUsername)
            registry.add("spring.datasource.password", container::getPassword)
        }
    }

    init {
        beforeEach {
            namedParameterJdbcTemplate.update("DELETE FROM book", MapSqlParameterSource())
        }

        test("save and findAll should work correctly") {
            val book1 = Book("Clean Code", "Robert Martin")
            val book2 = Book("TDD by Example", "Kent Beck")

            bookDAO.save(book1)
            bookDAO.save(book2)

            val result = bookDAO.findAll()

            result shouldContainExactlyInAnyOrder listOf(book1, book2)
        }

        test("findAll should return empty list when no books") {
            bookDAO.findAll().size shouldBe 0
        }
    }
}