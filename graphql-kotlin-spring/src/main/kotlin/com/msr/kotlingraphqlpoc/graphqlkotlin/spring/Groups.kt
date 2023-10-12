package com.msr.kotlingraphqlpoc.graphqlkotlin.spring

import com.expediagroup.graphql.server.operations.Query
import com.msr.kotlingraphqlpoc.GroupRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class GroupQueryService(private val groupRepository: GroupRepository) : Query {
    fun groups() = groupRepository.findAll()

    fun group(id: UUID) = groupRepository.findById(id)
}