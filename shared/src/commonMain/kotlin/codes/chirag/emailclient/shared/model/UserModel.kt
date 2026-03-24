package codes.chirag.emailclient.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val email: String,
    val isAuthenticated: Boolean = false
)
