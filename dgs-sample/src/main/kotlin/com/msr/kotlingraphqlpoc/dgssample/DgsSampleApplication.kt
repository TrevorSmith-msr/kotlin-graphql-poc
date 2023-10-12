package com.msr.kotlingraphqlpoc.dgssample

import com.msr.kotlingraphqlpoc.dgssample.types.Group
import com.msr.kotlingraphqlpoc.dgssample.types.MutationResult
import com.msr.kotlingraphqlpoc.dgssample.types.User
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.OffsetDateTime
import java.util.*

@SpringBootApplication
class DgsSampleApplication

fun main(args: Array<String>) {
    runApplication<DgsSampleApplication>(*args)
}

val GROUPS = listOf(
    Group.create("Group 1"),
    Group.create("Group 2"),
)

val DEFAULT_USERS = listOf(
    User.create("John", listOf(GROUPS.first())),
    User.create("Jerry", listOf(GROUPS.first(), GROUPS.last())),
    User.create("Jane", listOf(GROUPS.last())),
)

fun User.Companion.create(name: String, groups: List<Group>) =
    User(UUID.randomUUID(), name, groups, OffsetDateTime.now())

fun Group.Companion.create(name: String) = Group(UUID.randomUUID(), name, OffsetDateTime.now())

fun MutationResult.Companion.success(code: String? = null, message: String? = null) =
    MutationResult(code ?: "200", true, message ?: "Success")

fun MutationResult.Companion.error(code: String? = null, message: String? = null) =
    MutationResult(code ?: "404", false, message ?: "An error occurred")