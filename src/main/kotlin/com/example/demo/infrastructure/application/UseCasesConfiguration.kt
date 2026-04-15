package com.example.demo.infrastructure.application

import com.example.demo.domain.port.BookRepository
import com.example.demo.domain.usecase.BookUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCasesConfiguration {

    @Bean
    fun bookUseCase(bookRepository: BookRepository): BookUseCase {
        return BookUseCase(bookRepository)
    }
}