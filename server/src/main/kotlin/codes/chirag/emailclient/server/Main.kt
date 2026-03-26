package codes.chirag.emailclient.server

import codes.chirag.emailclient.server.di.serverModule
import codes.chirag.emailclient.server.plugins.configureDatabases
import codes.chirag.emailclient.server.plugins.configureRouting
import codes.chirag.emailclient.server.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(serverModule)
    }
    configureSerialization()
    configureDatabases()
    configureRouting()
}
