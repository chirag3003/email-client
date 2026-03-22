package codes.chirag.emailclient

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform