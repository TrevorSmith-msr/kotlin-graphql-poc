package com.msr.kotlingraphqlpoc

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import java.time.OffsetDateTime
import java.util.*

@GraphQLDescription("User")
data class User(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val groups: List<Group>,
    val createdAt: OffsetDateTime = OffsetDateTime.now()
)

@GraphQLDescription("Group")
data class Group(
    val id: UUID = UUID.randomUUID(), val name: String, val createdAt: OffsetDateTime = OffsetDateTime.now()
)

@GraphQLDescription("User search criteria input")
data class UserSearchCriteria(val name: String)

@GraphQLDescription("User invite input")
data class UserInvite(val name: String, val groupIds: List<UUID> = emptyList()) {
    fun toUser(): User {
        val idsToGroups = this.groupIds.asSequence().map { id -> id to GROUPS.find { it.id == id } }

        val missingIds = idsToGroups.filter { (_, group) -> group == null }
            .map { it.first }
        if (missingIds.any()) {
            throw GroupNotFoundException("Couldn't find group(s) ${missingIds.joinToString()}")
        }

        return User(name = name, groups = idsToGroups.map { it.second }.filterNotNull().toList())
    }
}

interface MutationResponse {
    val result: MutationResult
}

@GraphQLDescription("Result of executing a mutation")
data class MutationResult(val code: String, val success: Boolean, val message: String) {
    companion object {
        fun success(code: String? = null, message: String? = null) =
            MutationResult(code ?: "200", true, message ?: "Success")

        fun error(code: String? = null, message: String? = null) =
            MutationResult(code ?: "404", false, message ?: "An error occurred")
    }
}

@GraphQLDescription("User invite response")
data class InviteUserResponse(override val result: MutationResult, val user: User? = null) : MutationResponse {
    companion object {
        fun success(code: String? = null, message: String? = null, user: User) =
            InviteUserResponse(MutationResult.success(code, message), user)

        fun error(code: String? = null, message: String? = null) =
            InviteUserResponse(MutationResult.error(code, message))
    }
}

@GraphQLDescription("Delete user response")
data class DeleteUserResponse(override val result: MutationResult, val user: User? = null) : MutationResponse {
    companion object {
        fun success(code: String? = null, message: String? = null, user: User) =
            DeleteUserResponse(MutationResult.success(code, message), user)

        fun error(code: String? = null, message: String? = null) =
            DeleteUserResponse(MutationResult.error(code, message))
    }
}

class GroupNotFoundException(message: String) : RuntimeException(message)