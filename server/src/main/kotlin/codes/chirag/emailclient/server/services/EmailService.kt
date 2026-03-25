package codes.chirag.emailclient.server.services

import codes.chirag.emailclient.server.repository.EmailRepository
import codes.chirag.emailclient.shared.model.NormalizedEmail

class EmailService(private val repository: EmailRepository) {
    suspend fun getEmails(): List<NormalizedEmail> {
        // Business logic could go here, e.g., filtering, mapping, checking permissions
        return repository.getAllEmails()
    }

    suspend fun getEmail(id: String): NormalizedEmail? {
        return repository.getEmailById(id)
    }
}
