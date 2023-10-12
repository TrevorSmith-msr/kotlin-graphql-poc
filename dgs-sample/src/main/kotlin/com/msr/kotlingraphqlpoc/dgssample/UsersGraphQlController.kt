package com.msr.kotlingraphqlpoc.dgssample

import com.msr.kotlingraphqlpoc.dgssample.types.DeleteUserResponse
import com.msr.kotlingraphqlpoc.dgssample.types.InviteUserResponse
import com.msr.kotlingraphqlpoc.dgssample.types.MutationResult
import com.msr.kotlingraphqlpoc.dgssample.types.User
import com.msr.kotlingraphqlpoc.dgssample.types.UserInvite
import com.msr.kotlingraphqlpoc.dgssample.types.UserSearchCriteria
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger {}

val INVITED_USERS = mutableListOf<User>()

@DgsComponent
class UsersGraphQlController {

    @DgsQuery
    fun users() = DEFAULT_USERS + INVITED_USERS

    @DgsQuery
    fun user(@InputArgument id: UUID) = users().find { it.id == id }

    @DgsQuery
    fun searchUsers(@InputArgument criteria: UserSearchCriteria) = users().filter { criteria.matchUser(it) }

    @DgsQuery
    fun groups() = GROUPS

    @DgsQuery
    fun group(@InputArgument id: UUID) = GROUPS.find { group -> group.id == id }

    @DgsMutation
    fun inviteUser(@InputArgument userInvite: UserInvite) = try {
        InviteUserResponse(
            MutationResult.success(),
            userInvite.toUser().also(INVITED_USERS::add)
        )
    } catch (e: GroupNotFoundException) {
        InviteUserResponse(MutationResult.error("404", e.message))
    }

    @DgsMutation
    fun deleteUser(@InputArgument id: UUID): DeleteUserResponse {
        val user = user(id) ?: return DeleteUserResponse(MutationResult.error("404", "User not found"))
        logger.info { "Deleted user: $user" }
        return DeleteUserResponse(MutationResult.success(), user)
    }

    fun UserSearchCriteria.matchUser(user: User) = user.name.startsWith(this.name)

    fun UserInvite.toUser(): User {
        val idsToGroups = this.groupIds.asSequence().map { id -> id to GROUPS.find { it.id == id } }
        val missingIds = idsToGroups.filter { (_, group) -> group == null }
            .map { it.first }
        if (missingIds.any()) {
            throw GroupNotFoundException("Couldn't find group(s) ${missingIds.joinToString()}")
        }

        return User.create(this.name, idsToGroups.map { it.second }.filterNotNull().toList())
    }

    class GroupNotFoundException(message: String) : RuntimeException(message)
}