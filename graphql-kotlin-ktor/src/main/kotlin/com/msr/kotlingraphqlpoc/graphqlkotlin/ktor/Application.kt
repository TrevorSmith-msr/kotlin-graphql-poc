package com.msr.kotlingraphqlpoc.graphqlkotlin.ktor

import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import com.msr.kotlingraphqlpoc.GroupRepository
import com.msr.kotlingraphqlpoc.UserRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(GraphQL) {
        schema {
            packages = listOf("com.msr.kotlingraphqlpoc")

            val userRepository = UserRepository.create()
            val groupRepository = GroupRepository.create()
            queries = listOf(
                UserQueryService(userRepository),
                GroupQueryService(groupRepository)
            )
            mutations = listOf(UserMutationService(userRepository))
            hooks = CustomSchemaGeneratorHooks()
        }
    }
    routing {
        graphQLGetRoute()
        graphQLPostRoute()
        graphiQLRoute()
    }
}

