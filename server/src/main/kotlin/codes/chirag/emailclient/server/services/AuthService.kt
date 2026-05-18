package codes.chirag.emailclient.server.services

import codes.chirag.emailclient.server.repository.UserRepository
import codes.chirag.emailclient.shared.model.User
import io.ktor.server.plugins.*

class AuthService(private val userRepository: UserRepository) {
    suspend fun signup(name: String, email: String, passwordHash: String): User {
        if (userRepository.findByEmail(email) != null) {
            throw BadRequestException("User already exists")
        }
        return userRepository.create(name, email, passwordHash)
    }

    suspend fun login(email: String, passwordHash: String): User? {
        val (user, storedHash) = userRepository.findByEmail(email) ?: return null
        if (storedHash != passwordHash) return null
        return user
    }

    suspend fun getUser(email: String): User? {
        return userRepository.findByEmail(email)?.first
    }
}
