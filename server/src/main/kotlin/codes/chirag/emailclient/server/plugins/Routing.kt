package codes.chirag.emailclient.server.plugins

import codes.chirag.emailclient.server.routes.emailRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Email Client Sync Server")
        }
        
        emailRouting()
    }
}
