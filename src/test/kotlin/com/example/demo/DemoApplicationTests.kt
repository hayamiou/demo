package com.example.demo

import com.example.demo.domain.port.BookRepository
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DemoApplicationTests {

	@MockkBean
	lateinit var bookRepository: BookRepository

	@Test
	fun contextLoads() {}
}
