package com.msr.kotlingraphqlpoc.graphqlkotlin.spring

import com.expediagroup.graphql.server.types.GraphQLResponse
import com.msr.kotlingraphqlpoc.User
import io.kotest.assertions.asClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.tuple
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec
import org.springframework.test.web.reactive.server.expectBody
import java.util.function.Consumer

@Suppress("LeakingThis")
@SpringBootTest
@AutoConfigureWebTestClient
class UserQueriesSpec(private val testClient: WebTestClient) : ShouldSpec({
    extensions(SpringExtension)

    should("return users for users query") {
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

        testClient.get()
            .uri { builder -> builder.pathSegment("graphql").queryParam("query", query).build() }
            .exchange()
            .expectBody<GraphQLResponse<UsersQueryData>>().asClueConsumeWith { response ->
                response.status shouldBe HttpStatus.OK
                response.responseBody.asClue { result ->
                    result?.data shouldNotBe null
                    result?.data?.users.extract { tuple(name, groups.map { it.name }) }.shouldContainExactly(
                        tuple("John", listOf("Group 1")),
                        tuple("Jerry", listOf("Group 1", "Group 2")),
                        tuple("Jane", listOf("Group 2")),
                    )
                }
            }
    }
})

class UsersQueryData(val users: List<User>)

fun <B, S : BodySpec<B, S>> BodySpec<B, S>.asClueConsumeWith(consumer: Consumer<EntityExchangeResult<B>>) =
    consumeWith {
        it.asClue { c -> consumer.accept(c) }
    }

fun <K, T> Collection<K>?.extract(extractor: K.() -> T) = this?.map(extractor)