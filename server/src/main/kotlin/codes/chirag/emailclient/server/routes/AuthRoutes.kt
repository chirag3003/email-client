package codes.chirag.emailclient.server.routes

import codes.chirag.emailclient.server.services.AuthService
import codes.chirag.emailclient.shared.model.User
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import java.util.*

@Serializable
data class AuthResponse(val token: String, val user: User)

@Serializable
data class LoginRequest(val email: String, val passwordHash: String)

@Serializable
data class SignupRequest(val name: String, val email: String, val passwordHash: String)

fun Route.authRouting() {
    val authService by inject<AuthService>()

    val jwtAudience = "http://0.0.0.0:8080/hello"
    val jwtDomain = "https://jwt-provider-domain/"
    val jwtSecret = "secret"

    fun generateToken(email: String): String = JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtDomain)
        .withClaim("email", email)
        .withExpiresAt(Date(System.currentTimeMillis() + 3600000))
        .sign(Algorithm.HMAC256(jwtSecret))

    post("/signup") {
        val request = call.receive<SignupRequest>()
        val user = authService.signup(request.name, request.email, request.passwordHash)
        val token = generateToken(user.email)
        call.respond(AuthResponse(token, user))
    }

    post("/login") {
        val request = call.receive<LoginRequest>()
        val user = authService.login(request.email, request.passwordHash)
            ?: return@post call.respond(io.ktor.http.HttpStatusCode.Unauthorized, "Invalid credentials")
        val token = generateToken(user.email)
        call.respond(AuthResponse(token, user))
    }

    authenticate("auth-jwt") {
        get("/me") {
            val principal = call.principal<JWTPrincipal>()
            val email = principal?.payload?.getClaim("email")?.asString()
                ?: return@get call.respond(io.ktor.http.HttpStatusCode.Unauthorized)
            val user = authService.getUser(email)
                ?: return@get call.respond(io.ktor.http.HttpStatusCode.NotFound)
            call.respond(user)
        }
    }
}
