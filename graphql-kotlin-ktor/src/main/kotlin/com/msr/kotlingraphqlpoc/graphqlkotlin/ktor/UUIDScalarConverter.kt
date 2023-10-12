package com.msr.kotlingraphqlpoc.graphqlkotlin.ktor

import com.expediagroup.graphql.client.converter.ScalarConverter
import java.util.*

class UUIDScalarConverter : ScalarConverter<UUID> {
    override fun toJson(value: UUID) = value.toString()

    override fun toScalar(rawValue: Any): UUID = UUID.fromString(rawValue.toString())
}
