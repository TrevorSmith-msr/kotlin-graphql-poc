package com.msr.kotlingraphqlpoc.graphqlkotlin.spring

import com.msr.kotlingraphqlpoc.GroupRepository
import com.msr.kotlingraphqlpoc.UserRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {
    @Bean
    fun hooks() = CustomSchemaGeneratorHooks()

    @Bean
    fun groupRepository(): GroupRepository = GroupRepository.create()

    @Bean
    fun userRepository(): UserRepository = UserRepository.create()
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
