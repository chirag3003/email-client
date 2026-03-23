package codes.chirag.emailclient

interface Platform {
    val name: String
    val isDesktop: Boolean get() = false
}

expect fun getPlatform(): Platform
