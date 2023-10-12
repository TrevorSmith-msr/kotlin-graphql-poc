
import com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask
import java.util.*

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    id("com.netflix.dgs.codegen") version "6.0.3"
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.jackson.module.kotlin)
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:7.5.3"))
    implementation("com.netflix.graphql.dgs:graphql-dgs-webflux-starter")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")

    implementation(libs.kotlin.logging)

    testImplementation(platform(libs.kotest.bom))
    testImplementation(libs.bundles.kotest.spring)
}

tasks.withType<GenerateJavaTask> {
    packageName = "com.msr.kotlingraphqlpoc.dgssample"
    generateClient = true
    typeMapping = mutableMapOf(
        "UUID" to UUID::class
    ).mapValues { entry ->
        entry.value.qualifiedName!!
    }.toMutableMap()
}
