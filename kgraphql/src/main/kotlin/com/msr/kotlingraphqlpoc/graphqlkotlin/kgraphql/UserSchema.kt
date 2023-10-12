package com.msr.kotlingraphqlpoc.graphqlkotlin.kgraphql

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.msr.kotlingraphqlpoc.DeleteUserResponse
import com.msr.kotlingraphqlpoc.GroupNotFoundException
import com.msr.kotlingraphqlpoc.GroupRepository
import com.msr.kotlingraphqlpoc.InviteUserResponse
import com.msr.kotlingraphqlpoc.MutationResponse
import com.msr.kotlingraphqlpoc.UserInvite
import com.msr.kotlingraphqlpoc.UserRepository
import com.msr.kotlingraphqlpoc.UserSearchCriteria
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun SchemaBuilder.userSchema() {
    val groupRepository = GroupRepository.create()
    val userRepository = UserRepository.create()

    stringScalar<UUID> {
        serialize = UUID::toString
        deserialize = { rawValue -> UUID.fromString(rawValue) }
    }

    stringScalar<OffsetDateTime> {
        name = "DateTime"
        serialize = { dateTime -> dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) }
        deserialize = { rawValue -> OffsetDateTime.parse(rawValue) }
    }

    query("users") {
        description = "Return all users"

        resolver { -> userRepository.findAll() }
    }

    query("user") {
        description = "Find a user by ID"

        resolver { id: UUID -> userRepository.findById(id) }
            .withArgs { arg<UUID> { name = "id"; description = "ID of the user to find" } }
    }

    query("searchUsers") {
        description = "Search for users by name"

        resolver { searchCriteria: UserSearchCriteria -> userRepository.search(searchCriteria.name) }
            .withArgs { arg<String> { name = "searchCriteria"; description = "The criteria to search with" } }
    }

    query("groups") {
        description = "Return all groups"

        resolver { -> groupRepository.findAll() }
    }

    query("group") {
        description = "Find a group by ID"

        resolver { id: UUID -> groupRepository.findById(id) }
            .withArgs { arg<UUID> { name = "id"; description = "ID of the group to find" } }
    }

    mutation("inviteUser") {
        description = "Invite a user"

        resolver { userInvite: UserInvite ->
            try {
                InviteUserResponse.success(user = userRepository.create(userInvite.toUser()))
            } catch (e: GroupNotFoundException) {
                InviteUserResponse.error("404", e.message)
            }
        }
    }

    mutation("deleteUser") {
        description = "Delete a user by ID"

        resolver { id: UUID ->
            userRepository.findById(id)?.let {
                DeleteUserResponse.success(user = userRepository.delete(it))
            } ?: DeleteUserResponse.error("404", "User not found")
        }.withArgs { arg<UUID> { name = "id"; description = "ID of the user to delete" } }
    }

//    type<User>()
//    type<Group>()
//    type<MutationResult>()
    type<MutationResponse>()
//    type<InviteUserResponse>()
//    type<DeleteUserResponse>()
//
//    inputType<UserSearchCriteria>()
//    inputType<UserInvite>()
}