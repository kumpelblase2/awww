package de.eternalwings.awww

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class AwwwApplication

fun main(args: Array<String>) {
    SpringApplication.run(AwwwApplication::class.java, *args)
}
