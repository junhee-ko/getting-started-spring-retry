package com.example.gettingstartedspringretry

import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import java.lang.IndexOutOfBoundsException

@Configuration
@EnableRetry
class Application

@Service
class DeclarativeExampleService {

    @Retryable(retryFor = [RuntimeException::class])
    fun service() {
        println("service")
//        throw IOException()
        throw IndexOutOfBoundsException()
    }

    @Recover
    fun recover(e: RuntimeException?) {
        println("recover")
    }
}
