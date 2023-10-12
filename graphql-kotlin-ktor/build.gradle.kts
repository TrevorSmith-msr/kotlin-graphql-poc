plugins {
    alias(libs.plugins.ktor)
}

application {
    mainClass.set("msr.com.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.models)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.graphql.kotlin.ktor.server)
    implementation(libs.kotlin.logging)
    implementation(libs.logback)

    implementation(libs.graphql.kotlin.ktor.client)
    implementation(libs.graphql.java.extended.scalars)

    testImplementation(platform(libs.kotest.bom))
    testImplementation(libs.bundles.kotest.ktor)
}
