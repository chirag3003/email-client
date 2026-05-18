package codes.chirag.emailclient.server.plugins

import codes.chirag.emailclient.server.routes.authRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRouting()
    }
}
