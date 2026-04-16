package com.example.demo

import com.example.demo.domain.port.BookRepository
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource


@SpringBootTest
@TestPropertySource(properties = [
	"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration"
])
class DemoApplicationTests {

	@MockkBean
	lateinit var bookRepository: BookRepository

	@Test
	fun contextLoads() {}
}