package com.example.gettingstartedspringretry

import org.junit.jupiter.api.Test
import org.springframework.retry.RecoveryCallback
import org.springframework.retry.RetryCallback
import org.springframework.retry.RetryContext
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.policy.TimeoutRetryPolicy
import org.springframework.retry.support.RetryTemplate
import java.io.IOException
import java.util.*


class ImperativeExampleServiceTest {

    @Test
    fun quickStart() {
        val template: RetryTemplate = RetryTemplate.builder()
            .maxAttempts(3)
            .fixedBackoff(1000)
            .retryOn(RuntimeException::class.java)
            .build()

        template.execute<Any, RuntimeException> { ctx: RetryContext ->
            println("service")

            throw IllegalArgumentException() // subclass of RuntimeException
//            throw IOException()
        }
    }

    @Test
    fun timeoutPolicy() {
        val template = RetryTemplate()

        val policy = TimeoutRetryPolicy()
        policy.timeout = 5000L

        template.setRetryPolicy(policy)

        template.execute(object : RetryCallback<MyObject, Exception> {

            override fun doWithRetry(context: RetryContext): MyObject {
                println("doWithRetry")
                // Do stuff that might fail, e.g. webservice operation
                return MyObject("test")
            }
        })
    }

    @Test
    fun fluentConfiguration() {
        RetryTemplate.builder()
            .maxAttempts(10)
            .exponentialBackoff(100, 2.0, 10000)
            .retryOn(IOException::class.java)
            .traversingCauses()
            .build()

        RetryTemplate.builder()
            .fixedBackoff(10)
            .withTimeout(3000)
            .build()

        RetryTemplate.builder()
            .infiniteRetry()
            .retryOn(IOException::class.java)
            .uniformRandomBackoff(1000, 3000)
            .build()
    }

    @Test
    fun retryContext() {
        val template: RetryTemplate = RetryTemplate.builder()
            .maxAttempts(3)
            .fixedBackoff(1000)
            .retryOn(RuntimeException::class.java)
            .build()

        template.execute(object : RetryCallback<MyObject, Exception> {

            override fun doWithRetry(context: RetryContext): MyObject {
                println("doWithRetry, retryCount: ${context.retryCount}")

                // Do stuff that might fail, e.g. webservice operation
                throw java.lang.RuntimeException()
                return MyObject("test")
            }
        })
    }

    @Test
    fun recoveryCallback() {
        val template: RetryTemplate = RetryTemplate.builder()
            .maxAttempts(3)
            .fixedBackoff(1000)
            .retryOn(RuntimeException::class.java)
            .build()

        template.execute(
            object : RetryCallback<MyObject, Exception> {
                override fun doWithRetry(context: RetryContext): MyObject {
                    // business logic here
                    println("doWithRetry")
                    throw java.lang.IllegalArgumentException()
                    return MyObject("doWithRetry")
                }
            },

            object : RecoveryCallback<MyObject> {
                override fun recover(context: RetryContext?): MyObject {
                    // recover logic here
                    println("recover")
                    return MyObject("recover")
                }
            }
        )
    }

    @Test
    fun simpleRetryPolicy() {
        val policy = SimpleRetryPolicy(
            /* maxAttempts = */ 5,
            /* retryableExceptions = */ Collections.singletonMap(java.lang.RuntimeException::class.java, true)
        )
        val template = RetryTemplate()
        template.setRetryPolicy(policy)
        template.execute(object : RetryCallback<MyObject, RuntimeException> {
            override fun doWithRetry(context: RetryContext?): MyObject {
                // business logic here
                println("doWithRetry")
//                throw IOException("")
                throw java.lang.IllegalArgumentException("")
                return MyObject("test")
            }
        })

    }
}

data class MyObject(
    val test: String
)