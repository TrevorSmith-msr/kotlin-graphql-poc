package com.msr.kotlingraphqlpoc.graphqlkotlin.kgraphql

import com.apurebase.kgraphql.GraphQL
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(GraphQL) {
        playground = true

        schema { userSchema() }
    }
}

