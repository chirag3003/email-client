package codes.chirag.emailclient.server.db

import org.jetbrains.exposed.sql.Table

object EmailTable : Table("emails") {
    val internalId = varchar("internal_id", 255)
    val workspace = varchar("workspace", 50)
    val folder = varchar("folder", 50)
    val senderName = varchar("sender_name", 255)
    val senderEmail = varchar("sender_email", 255)
    val subject = varchar("subject", 500)
    val snippet = text("snippet")
    val bodyText = text("body_text")
    val timestampStr = varchar("timestamp_str", 100)
    val timestamp = long("timestamp")
    val isRead = bool("is_read")
    val isThreadSelected = bool("is_thread_selected").default(false)

    override val primaryKey = PrimaryKey(internalId)
}
