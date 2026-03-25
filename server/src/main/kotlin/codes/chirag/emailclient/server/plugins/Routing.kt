package codes.chirag.emailclient.server.plugins

import codes.chirag.emailclient.server.repository.EmailRepositoryImpl
import codes.chirag.emailclient.server.routes.emailRouting
import codes.chirag.emailclient.server.services.EmailService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    // In a real app, use dependency injection (e.g. Koin) for this:
    val emailRepository = EmailRepositoryImpl()
    val emailService = EmailService(emailRepository)

    routing {
        get("/") {
            call.respondText("Email Client Sync Server")
        }
        
        emailRouting(emailService)
    }
}
