package codes.chirag.emailclient.core.data

import codes.chirag.emailclient.core.domain.FolderType
import codes.chirag.emailclient.core.domain.NormalizedEmail
import codes.chirag.emailclient.core.domain.WorkspaceType

object MockData {
    val MockEmails = listOf(
        NormalizedEmail(
            internalId = "1",
            workspace = WorkspaceType.GMAIL,
            folder = FolderType.INBOX,
            senderName = "The Paris Review",
            senderEmail = "newsletter@theparisreview.org",
            subject = "Your weekly edition",
            snippet = "This week, we look back at the art of the interview. Since our founding in 1953...",
            bodyText = """
                Dear Reader,
                
                This week, we look back at the art of the interview. Since our founding in 1953, the Writers at Work series has stood as a core pillar of our editorial mission. The premise is simple: writers speaking to writers, candidly and at length, about the mechanics of their craft.
                
                In our archive deep-dive today, we revisit a conversation that continues to resonate. "I write to discover what I know," she said, seated in her sun-drenched living room. It's a sentiment that captures the inherently exploratory nature of sitting down at the blank page.
                
                Also in this issue:
                
                • New fiction from emerging voices in the American South.
                • A critical essay on the evolution of concrete poetry.
                • Selections from the upcoming Winter issue portfolio.
                
                As always, thank you for reading. Support for independent literary publishing has never been more vital.
            """.trimIndent(),
            timestampStr = "10:42 AM",
            timestamp = 1698331320000, // Roughly Oct 26, 10:42 AM
            isRead = false,
            isThreadSelected = true
        ),
        NormalizedEmail(
            internalId = "2",
            workspace = WorkspaceType.GMAIL,
            folder = FolderType.INBOX,
            senderName = "GitHub",
            senderEmail = "noreply@github.com",
            subject = "Security alert - new sign in",
            snippet = "A new sign-in to GitHub was detected from an unrecognized device.",
            bodyText = "Security alert: A new sign-in was detected. Please review the details.",
            timestampStr = "Yesterday",
            timestamp = 1698244920000,
            isRead = true
        ),
        NormalizedEmail(
            internalId = "3",
            workspace = WorkspaceType.GMAIL,
            folder = FolderType.INBOX,
            senderName = "Stripe",
            senderEmail = "receipts@stripe.com",
            subject = "Invoice receipt for October",
            snippet = "Your receipt for $49.00 is available for viewing.",
            bodyText = "Here is your invoice for the recent transaction of $49.00. Thank you for your business.",
            timestampStr = "Oct 24",
            timestamp = 1698158520000,
            isRead = true
        ),
        NormalizedEmail(
            internalId = "4",
            workspace = WorkspaceType.GMAIL,
            folder = FolderType.INBOX,
            senderName = "Linear",
            senderEmail = "hello@linear.app",
            subject = "New features: Cycle Planning",
            snippet = "We've rebuilt cycles to make it easier to plan and track your work.",
            bodyText = "Linear Update: Check out the new cycle planning features available now.",
            timestampStr = "Oct 22",
            timestamp = 1697985720000,
            isRead = true
        ),
        NormalizedEmail(
            internalId = "5",
            workspace = WorkspaceType.GMAIL,
            folder = FolderType.DRAFTS,
            senderName = "Draft",
            senderEmail = "",
            subject = "Project Update: Q4 Goals",
            snippet = "Here are the latest notes regarding our upcoming goals...",
            bodyText = "Here are the latest notes regarding our upcoming goals for Q4. We need to focus on...",
            timestampStr = "11:30 AM",
            timestamp = 1698334800000,
            isRead = true
        ),
        NormalizedEmail(
            internalId = "6",
            workspace = WorkspaceType.GMAIL,
            folder = FolderType.DRAFTS,
            senderName = "Draft",
            senderEmail = "",
            subject = "Follow up: Design Review",
            snippet = "I've attached the new Figma links for the checkout flow.",
            bodyText = "I've attached the new Figma links for the checkout flow. Let me know what you think.",
            timestampStr = "Yesterday",
            timestamp = 1698244800000,
            isRead = true
        )
    )
}
