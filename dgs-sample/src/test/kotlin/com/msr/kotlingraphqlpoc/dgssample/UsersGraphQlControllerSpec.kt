package com.msr.kotlingraphqlpoc.dgssample

import com.jayway.jsonpath.TypeRef
import com.msr.kotlingraphqlpoc.dgssample.types.User
import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import com.netflix.graphql.dgs.autoconfig.DgsExtendedScalarsAutoConfiguration
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContainExactly
import org.assertj.core.api.Assertions.tuple
import org.springframework.boot.test.context.SpringBootTest

object UsersListTypeRef : TypeRef<List<User>>()

@SpringBootTest(classes = [DgsAutoConfiguration::class, DgsExtendedScalarsAutoConfiguration::class, UsersGraphQlController::class])
class UsersGraphQlControllerSpec(private val dgsQueryExecutor: DgsQueryExecutor) : ShouldSpec({
    extensions(SpringExtension)

    should("return users for users query") {
        val users = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
            """
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
        """.trimIndent(), "data.users[*]", UsersListTypeRef
        )

        withClue("users query results") {
            users.extract { tuple(name, groups.map { it.name }) }
                .shouldContainExactly(
                    tuple("John", listOf("Group 1")),
                    tuple("Jerry", listOf("Group 1", "Group 2")),
                    tuple("Jane", listOf("Group 2")),
                )
        }
    }
})

fun <K, T> Collection<K>?.extract(extractor: K.() -> T) = this?.map(extractor)