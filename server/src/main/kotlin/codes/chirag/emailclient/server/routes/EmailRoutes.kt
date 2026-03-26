package codes.chirag.emailclient.server.routes

import codes.chirag.emailclient.server.services.EmailService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.emailRouting() {
    val emailService by inject<EmailService>()

    route("/emails") {
        get {
            val emails = emailService.getEmails()
            call.respond(emails)
        }
        
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText("Missing id")
            val email = emailService.getEmail(id) ?: return@get call.respondText("No email with id $id")
            call.respond(email)
        }
    }
}
