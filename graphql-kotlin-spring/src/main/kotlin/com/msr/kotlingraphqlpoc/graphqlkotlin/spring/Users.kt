package com.msr.kotlingraphqlpoc.graphqlkotlin.spring

import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import com.msr.kotlingraphqlpoc.DeleteUserResponse
import com.msr.kotlingraphqlpoc.GroupNotFoundException
import com.msr.kotlingraphqlpoc.InviteUserResponse
import com.msr.kotlingraphqlpoc.User
import com.msr.kotlingraphqlpoc.UserInvite
import com.msr.kotlingraphqlpoc.UserRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserQueryService(private val userRepository: UserRepository) : Query {
    fun users() = userRepository.findAll()

    fun user(id: UUID) = userRepository.findById(id)

    fun searchUsers(name: String) = userRepository.search(name)
}

@Component
class UserMutationService(private val userRepository: UserRepository) : Mutation {
    fun inviteUser(userInvite: UserInvite) =
        try {
            InviteUserResponse.success(user = userRepository.create(userInvite.toUser()))
        } catch (e: GroupNotFoundException) {
            InviteUserResponse.error("404", e.message)
        }

    fun deleteUser(user: User) = DeleteUserResponse.success(user = userRepository.delete(user))
}
