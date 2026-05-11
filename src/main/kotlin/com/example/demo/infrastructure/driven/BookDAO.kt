package com.example.demo.infrastructure.driven

import com.example.demo.domain.model.Book
import com.example.demo.domain.port.BookRepository
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class BookDAO(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : BookRepository {

    override fun save(book: Book) {
        namedParameterJdbcTemplate.update(
            "INSERT INTO book (title, author, reserved) VALUES (:title, :author, :reserved)",
            MapSqlParameterSource()
                .addValue("title", book.title)
                .addValue("author", book.author)
                .addValue("reserved", book.reserved)
        )
    }

    override fun findAll(): List<Book> {
        return namedParameterJdbcTemplate.query(
            "SELECT * FROM book",
            MapSqlParameterSource()
        ) { rs, _ ->
            Book(
                id = rs.getLong("id"),
                title = rs.getString("title"),
                author = rs.getString("author"),
                reserved = rs.getBoolean("reserved")
            )
        }
    }

    override fun findById(id: Long): Book? {
        return namedParameterJdbcTemplate.query(
            "SELECT * FROM book WHERE id = :id",
            MapSqlParameterSource().addValue("id", id)
        ) { rs, _ ->
            Book(
                id = rs.getLong("id"),
                title = rs.getString("title"),
                author = rs.getString("author"),
                reserved = rs.getBoolean("reserved")
            )
        }.firstOrNull()
    }

    override fun reserve(id: Long) {
        namedParameterJdbcTemplate.update(
            "UPDATE book SET reserved = true WHERE id = :id",
            MapSqlParameterSource().addValue("id", id)
        )
    }
}