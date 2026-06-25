package codes.chirag.emailclient.server.plugins

import codes.chirag.emailclient.server.jwtAudience
import codes.chirag.emailclient.server.jwtDomain
import codes.chirag.emailclient.server.jwtRealm
import codes.chirag.emailclient.server.jwtSecret
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    val config = environment.config
    val jwtAudience = config.jwtAudience()
    val jwtDomain = config.jwtDomain()
    val jwtRealm = config.jwtRealm()
    val jwtSecret = config.jwtSecret()

    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("email").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(io.ktor.http.HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
