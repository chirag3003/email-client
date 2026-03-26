plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktor)
}

group = "codes.chirag.emailclient"
version = "1.0.0"

dependencies {
    implementation(projects.shared)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.logback.classic)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.postgresql)
    implementation(libs.hikaricp)
}
