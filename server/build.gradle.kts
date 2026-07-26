plugins {
    kotlin("jvm")
    application
}

group = "codes.chirag.emailclient"
version = "1.0.0"

application {
    mainClass.set("codes.chirag.emailclient.server.MainKt")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)

    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test)
}
