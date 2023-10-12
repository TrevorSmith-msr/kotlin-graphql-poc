package com.msr.kotlingraphqlpoc.graphqlkotlin.ktor

import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import com.msr.kotlingraphqlpoc.User
import com.msr.kotlingraphqlpoc.UserInvite
import com.msr.kotlingraphqlpoc.UserRepository
import java.util.*

class UserQueryService(private val userRepository: UserRepository) : Query {
    fun users() = userRepository.findAll()

    fun user(id: UUID) = userRepository.findById(id)

    fun searchUsers(name: String) = userRepository.search(name)
}

class UserMutationService(private val userRepository: UserRepository) : Mutation {
    fun inviteUser(userInvite: UserInvite) = userRepository.create(userInvite.toUser())

    fun deleteUser(user: User) = userRepository.delete(user)
}