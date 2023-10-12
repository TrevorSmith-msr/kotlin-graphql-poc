package com.msr.kotlingraphqlpoc.graphqlkotlin.ktor

import com.msr.kotlingraphqlpoc.User
import io.kotest.assertions.asClue
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.tuple
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldNotBe
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*

class UsersQuerySpec : ShouldSpec({

    appShould("return users for users query") {
        val client = createClient(jsonClientConfig)

        val query = """
            {
                users {
                    id
                    name
                    groups {
                        id
                        name
                        createdAt
                    }
                    createdAt
                }
            }
            """.trimIndent()

        client.post("/graphql") {
            contentType(ContentType.Application.Json)
            setBody("""{ "query": "$query" }""")
        }.asClue { response ->
            response shouldHaveStatus HttpStatusCode.OK
            response.body<GraphQlResponse<UsersQueryData>>().asClue { result ->
                result.data shouldNotBe null
                result.data!!.users.extract { tuple(name, groups.map { it.name }) }.shouldContainExactly(
                    tuple("John", listOf("Group 1")),
                    tuple("Jerry", listOf("Group 1", "Group 2")),
                    tuple("Jane", listOf("Group 2")),
                )
            }
        }
    }
})

class GraphQlResponse<T>(val data: T?)
class UsersQueryData(val users: List<User>)

fun ShouldSpec.appShould(name: String, test: suspend ApplicationTestBuilder.() -> Unit) = should(name) {
    testApplication(test)
}

val jsonClientConfig: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit = {
    install(ContentNegotiation) {
        jackson() {
            findAndRegisterModules()
        }
    }
}

fun <K, T> Collection<K>?.extract(extractor: K.() -> T) = this?.map(extractor)