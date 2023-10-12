package com.msr.kotlingraphqlpoc.dgssample

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DgsSampleApplicationSpec : ShouldSpec({
    should("load the spring context without errors") {}
}) {
    override fun extensions() = listOf(SpringExtension)
}
