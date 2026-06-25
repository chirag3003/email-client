package codes.chirag.emailclient

import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import codes.chirag.emailclient.core.domain.AppMode
import codes.chirag.emailclient.core.domain.GlobalState
import codes.chirag.emailclient.core.input.KeyboardManager
import codes.chirag.emailclient.shared.model.*
import kotlin.test.*

@OptIn(InternalComposeUiApi::class)
private fun createKeyEvent(
    key: Key,
    type: KeyEventType = KeyEventType.KeyDown,
    isShiftPressed: Boolean = false
): KeyEvent = KeyEvent(
    key = key,
    type = type,
    isShiftPressed = isShiftPressed
)

class KeyboardManagerTest {

    private val testEmails = listOf(
        NormalizedEmail(
            internalId = "1", workspace = WorkspaceType.GMAIL, folder = FolderType.INBOX,
            senderName = "Alice", senderEmail = "alice@example.com", subject = "Test 1",
            snippet = "Snippet 1", bodyText = "Body 1", timestampStr = "Jan 1",
            timestamp = 1000L, isRead = false
        ),
        NormalizedEmail(
            internalId = "2", workspace = WorkspaceType.GMAIL, folder = FolderType.INBOX,
            senderName = "Bob", senderEmail = "bob@example.com", subject = "Test 2",
            snippet = "Snippet 2", bodyText = "Body 2", timestampStr = "Jan 2",
            timestamp = 2000L, isRead = true
        ),
        NormalizedEmail(
            internalId = "3", workspace = WorkspaceType.GMAIL, folder = FolderType.INBOX,
            senderName = "Charlie", senderEmail = "charlie@example.com", subject = "Test 3",
            snippet = "Snippet 3", bodyText = "Body 3", timestampStr = "Jan 3",
            timestamp = 3000L, isRead = false
        ),
        NormalizedEmail(
            internalId = "4", workspace = WorkspaceType.GMAIL, folder = FolderType.TRASH,
            senderName = "Dave", senderEmail = "dave@example.com", subject = "Trashed",
            snippet = "Snippet 4", bodyText = "Body 4", timestampStr = "Jan 4",
            timestamp = 4000L, isRead = true
        )
    )

    private lateinit var manager: KeyboardManager
    private var executedCommandLabel: String? = null

    @BeforeTest
    fun setup() {
        executedCommandLabel = null
        manager = KeyboardManager(
            onExecuteCommand = { state ->
                executedCommandLabel = "executed"
                state.copy(currentMode = AppMode.QUEUE_NAVIGATION, commandQuery = "")
            },
            getFilteredCommandCount = { 10 }
        )
    }

    @Test
    fun `j key moves selection to first email when none selected`() {
        val state = GlobalState(emails = testEmails)
        val result = manager.handleEvent(createKeyEvent(Key.J), state)
        assertEquals("1", result.activeEmailId)
    }

    @Test
    fun `j key moves to next email`() {
        val state = GlobalState(emails = testEmails, activeEmailId = "1")
        val result = manager.handleEvent(createKeyEvent(Key.J), state)
        assertEquals("2", result.activeEmailId)
    }

    @Test
    fun `j key does not go past last email`() {
        val state = GlobalState(emails = testEmails, activeEmailId = "3")
        val result = manager.handleEvent(createKeyEvent(Key.J), state)
        assertEquals("3", result.activeEmailId)
    }

    @Test
    fun `k key moves to previous email`() {
        val state = GlobalState(emails = testEmails, activeEmailId = "2")
        val result = manager.handleEvent(createKeyEvent(Key.K), state)
        assertEquals("1", result.activeEmailId)
    }

    @Test
    fun `k key does not go before first email`() {
        val state = GlobalState(emails = testEmails, activeEmailId = "1")
        val result = manager.handleEvent(createKeyEvent(Key.K), state)
        assertEquals("1", result.activeEmailId)
    }

    @Test
    fun `enter key opens active email`() {
        val state = GlobalState(emails = testEmails, activeEmailId = "1", showActiveEmail = false)
        val result = manager.handleEvent(createKeyEvent(Key.Enter), state)
        assertTrue(result.showActiveEmail)
    }

    @Test
    fun `e key archives active email`() {
        val state = GlobalState(emails = testEmails, activeEmailId = "1")
        val result = manager.handleEvent(createKeyEvent(Key.E), state)
        val archivedEmail = result.emails.find { it.internalId == "1" }
        assertEquals(FolderType.ARCHIVE, archivedEmail?.folder)
        assertNull(result.activeEmailId)
    }

    @Test
    fun `d key trashes active email`() {
        val state = GlobalState(emails = testEmails, activeEmailId = "1")
        val result = manager.handleEvent(createKeyEvent(Key.D), state)
        val trashedEmail = result.emails.find { it.internalId == "1" }
        assertEquals(FolderType.TRASH, trashedEmail?.folder)
        assertNull(result.activeEmailId)
    }

    @Test
    fun `d key trashes selected emails in bulk`() {
        val state = GlobalState(
            emails = testEmails,
            selectedEmailIds = setOf("1", "2")
        )
        val result = manager.handleEvent(createKeyEvent(Key.D), state)
        assertEquals(FolderType.TRASH, result.emails.find { it.internalId == "1" }?.folder)
        assertEquals(FolderType.TRASH, result.emails.find { it.internalId == "2" }?.folder)
        assertTrue(result.selectedEmailIds.isEmpty())
    }

    @Test
    fun `r key restores trashed email to inbox`() {
        val state = GlobalState(emails = testEmails, activeEmailId = "4")
        val result = manager.handleEvent(createKeyEvent(Key.R), state)
        val restoredEmail = result.emails.find { it.internalId == "4" }
        assertEquals(FolderType.INBOX, restoredEmail?.folder)
    }

    @Test
    fun `c key opens compose mode`() {
        val state = GlobalState(emails = testEmails, activeEmailId = "1")
        val result = manager.handleEvent(createKeyEvent(Key.C), state)
        assertTrue(result.isComposing)
        assertNull(result.activeEmailId)
    }

    @Test
    fun `x key toggles email selection`() {
        val state = GlobalState(emails = testEmails, activeEmailId = "1")
        val result = manager.handleEvent(createKeyEvent(Key.X), state)
        assertTrue("1" in result.selectedEmailIds)
    }

    @Test
    fun `x key deselects already selected email`() {
        val state = GlobalState(emails = testEmails, activeEmailId = "1", selectedEmailIds = setOf("1"))
        val result = manager.handleEvent(createKeyEvent(Key.X), state)
        assertTrue("1" !in result.selectedEmailIds)
    }

    @Test
    fun `question mark shows cheatsheet`() {
        val state = GlobalState(emails = testEmails)
        val result = manager.handleEvent(createKeyEvent(Key.Slash, isShiftPressed = true), state)
        assertTrue(result.isCheatsheetVisible)
    }

    @Test
    fun `escape clears selection when emails are selected`() {
        val state = GlobalState(emails = testEmails, selectedEmailIds = setOf("1", "2"))
        val result = manager.handleEvent(createKeyEvent(Key.Escape), state)
        assertTrue(result.selectedEmailIds.isEmpty())
    }

    @Test
    fun `escape closes email detail when active`() {
        val state = GlobalState(emails = testEmails, activeEmailId = "1", showActiveEmail = true)
        val result = manager.handleEvent(createKeyEvent(Key.Escape), state)
        assertFalse(result.showActiveEmail)
    }

    @Test
    fun `g prefix then i navigates to inbox`() {
        val state = GlobalState(emails = testEmails, activeFolder = FolderType.TRASH)
        manager.handleEvent(createKeyEvent(Key.G), state)
        val result = manager.handleEvent(createKeyEvent(Key.I), state)
        assertEquals(FolderType.INBOX, result.activeFolder)
        assertNull(result.activeEmailId)
    }

    @Test
    fun `g prefix then s navigates to sent`() {
        val state = GlobalState(emails = testEmails)
        manager.handleEvent(createKeyEvent(Key.G), state)
        val result = manager.handleEvent(createKeyEvent(Key.S), state)
        assertEquals(FolderType.SENT, result.activeFolder)
    }

    @Test
    fun `g prefix then t navigates to trash`() {
        val state = GlobalState(emails = testEmails)
        manager.handleEvent(createKeyEvent(Key.G), state)
        val result = manager.handleEvent(createKeyEvent(Key.T), state)
        assertEquals(FolderType.TRASH, result.activeFolder)
    }

    @Test
    fun `non-keydown events are ignored`() {
        val state = GlobalState(emails = testEmails, activeEmailId = "1")
        val result = manager.handleEvent(createKeyEvent(Key.J, KeyEventType.KeyUp), state)
        assertEquals(state, result)
    }

    @Test
    fun `j key only navigates emails in active folder`() {
        val state = GlobalState(emails = testEmails, activeFolder = FolderType.TRASH)
        val result = manager.handleEvent(createKeyEvent(Key.J), state)
        assertEquals("4", result.activeEmailId)
    }
}
