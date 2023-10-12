package com.msr.kotlingraphqlpoc.graphqlkotlin.ktor

import com.expediagroup.graphql.server.operations.Query
import com.msr.kotlingraphqlpoc.GroupRepository
import java.util.*

class GroupQueryService(private val groupRepository: GroupRepository) : Query {
    fun groups() = groupRepository.findAll()

    fun group(id: UUID) = groupRepository.findById(id)
}