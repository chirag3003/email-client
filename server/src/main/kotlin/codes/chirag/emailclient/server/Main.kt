package codes.chirag.emailclient.server

import codes.chirag.emailclient.server.di.serverModule
import codes.chirag.emailclient.server.plugins.configureDatabases
import codes.chirag.emailclient.server.plugins.configureRouting
import codes.chirag.emailclient.server.plugins.configureSecurity
import codes.chirag.emailclient.server.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    embeddedServer(Netty, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(serverModule)
    }
    configureSerialization()
    configureSecurity()
    configureDatabases()
    configureRouting()
}

fun ApplicationConfig.jwtSecret(): String = property("jwt.secret").getString()
fun ApplicationConfig.jwtAudience(): String = property("jwt.audience").getString()
fun ApplicationConfig.jwtDomain(): String = property("jwt.domain").getString()
fun ApplicationConfig.jwtRealm(): String = property("jwt.realm").getString()
fun ApplicationConfig.jwtExpiryMillis(): Long = property("jwt.expiryMillis").getString().toLong()
