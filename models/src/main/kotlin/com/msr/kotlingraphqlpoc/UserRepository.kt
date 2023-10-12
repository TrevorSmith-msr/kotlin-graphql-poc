package com.msr.kotlingraphqlpoc

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.*

val DEFAULT_USERS = listOf(
    User(name = "John", groups = listOf(GROUPS.first())),
    User(name = "Jerry", groups = listOf(GROUPS.first(), GROUPS.last())),
    User(name = "Jane", groups = listOf(GROUPS.last())),
)

private val logger = KotlinLogging.logger {}

interface UserRepository {
    companion object {
        fun create(): UserRepository = InMemoryUserRepository()
    }
    fun findAll(): List<User>
    fun findById(id: UUID): User?
    fun search(name: String): List<User>
    fun create(user: User): User
    fun delete(user: User): User
}

class InMemoryUserRepository : UserRepository {
    private val addedUsers = mutableListOf<User>()

    override fun findAll() = DEFAULT_USERS + addedUsers

    override fun findById(id: UUID) = findAll().find { it.id == id }

    override fun search(name: String) = findAll().filter { it.name.startsWith(name) }

    override fun create(user: User) = user.also { addedUsers.add(it) }

    // Not really deleting the user yet
    override fun delete(user: User) = user.also { logger.info { "Deleting user: $user" } }
}