package codes.chirag.emailclient.server

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import codes.chirag.emailclient.shared.model.NormalizedEmail
import codes.chirag.emailclient.shared.model.WorkspaceType
import codes.chirag.emailclient.shared.model.FolderType

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/") {
            call.respondText("Email Client Sync Server")
        }
        get("/emails") {
            // Placeholder for real sync logic
            call.respond(emptyList<NormalizedEmail>())
        }
    }
}
