plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(projects.models)

    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.graphql.kotlin.spring.server)
    implementation(libs.kotlin.logging)
    implementation(libs.logback)

    implementation(libs.graphql.kotlin.ktor.client)
    implementation(libs.graphql.java.extended.scalars)

    testImplementation(platform(libs.kotest.bom))
    testImplementation(libs.bundles.kotest.spring) {
        exclude(group = "org.mockito")
    }
}
