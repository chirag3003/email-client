package codes.chirag.emailclient.server.repository

import codes.chirag.emailclient.shared.model.FolderType
import codes.chirag.emailclient.shared.model.NormalizedEmail
import codes.chirag.emailclient.shared.model.WorkspaceType

interface EmailRepository {
    suspend fun getAllEmails(): List<NormalizedEmail>
    suspend fun getEmailById(id: String): NormalizedEmail?
}

class EmailRepositoryImpl : EmailRepository {
    private val mockEmails = listOf(
        NormalizedEmail(
            internalId = "1",
            workspace = WorkspaceType.GMAIL,
            folder = FolderType.INBOX,
            senderName = "Example Sender",
            senderEmail = "sender@example.com",
            subject = "Hello from the backend",
            snippet = "This is a backend mock",
            bodyText = "Here is some more content from the backend mock implementation.",
            timestampStr = "Today",
            timestamp = System.currentTimeMillis(),
            isRead = false,
            isThreadSelected = false
        )
    )

    override suspend fun getAllEmails(): List<NormalizedEmail> {
        return mockEmails
    }

    override suspend fun getEmailById(id: String): NormalizedEmail? {
        return mockEmails.find { it.internalId == id }
    }
}
