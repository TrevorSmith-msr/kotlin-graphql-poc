package com.msr.kotlingraphqlpoc

import java.util.*

val GROUPS = listOf(
    Group(name = "Group 1"),
    Group(name = "Group 2"),
)

interface GroupRepository {
    companion object {
        fun create(): GroupRepository = InMemoryGroupRepository()
    }
    fun findAll(): List<Group>
    fun findById(id: UUID): Group?
}

class InMemoryGroupRepository : GroupRepository {
    override fun findAll() = GROUPS
    override fun findById(id: UUID) = findAll().find { it.id == id }
}