import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
}

allprojects {
    group = "com.msr"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    val javaTarget = JavaVersion.VERSION_20
    java.sourceCompatibility = javaTarget

    dependencies {
        implementation(platform(rootProject.libs.spring.boot.bom))

        testImplementation(platform(rootProject.libs.kotest.bom))
        testImplementation(rootProject.libs.kotest.runner.junit5)
        testImplementation(rootProject.libs.mockk)
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        filter {
            isFailOnNoMatchingTests = false
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = javaTarget.toString()
        }
    }
}
