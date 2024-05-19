package com.example.gettingstartedspringretry

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DeclarativeExampleTest{

    @Autowired
    private lateinit var declarativeExampleService: DeclarativeExampleService

    @Test
    fun retry() {
        declarativeExampleService.service()
    }
}