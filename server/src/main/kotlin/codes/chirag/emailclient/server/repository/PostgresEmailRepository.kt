package codes.chirag.emailclient.server.repository

import codes.chirag.emailclient.server.db.EmailTable
import codes.chirag.emailclient.shared.model.FolderType
import codes.chirag.emailclient.shared.model.NormalizedEmail
import codes.chirag.emailclient.shared.model.WorkspaceType
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresEmailRepository : EmailRepository {
    override suspend fun getAllEmails(): List<NormalizedEmail> {
        return transaction {
            EmailTable.selectAll().map { toEmail(it) }
        }
    }

    override suspend fun getEmailById(id: String): NormalizedEmail? {
        return transaction {
            EmailTable.selectAll().where { EmailTable.internalId eq id }
                .map { toEmail(it) }
                .singleOrNull()
        }
    }

    private fun toEmail(row: ResultRow): NormalizedEmail {
        return NormalizedEmail(
            internalId = row[EmailTable.internalId],
            workspace = WorkspaceType.valueOf(row[EmailTable.workspace]),
            folder = FolderType.valueOf(row[EmailTable.folder]),
            senderName = row[EmailTable.senderName],
            senderEmail = row[EmailTable.senderEmail],
            subject = row[EmailTable.subject],
            snippet = row[EmailTable.snippet],
            bodyText = row[EmailTable.bodyText],
            timestampStr = row[EmailTable.timestampStr],
            timestamp = row[EmailTable.timestamp],
            isRead = row[EmailTable.isRead],
            isThreadSelected = row[EmailTable.isThreadSelected]
        )
    }
}
