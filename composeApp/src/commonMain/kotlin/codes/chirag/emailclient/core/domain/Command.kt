package codes.chirag.emailclient.core.domain

data class Command(
    val label: String,
    val shortcut: String? = null,
    val action: () -> Unit
)
