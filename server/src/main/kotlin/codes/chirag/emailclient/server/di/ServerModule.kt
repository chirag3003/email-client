package codes.chirag.emailclient.server.di

import codes.chirag.emailclient.server.repository.PostgresUserRepository
import codes.chirag.emailclient.server.repository.UserRepository
import codes.chirag.emailclient.server.services.AuthService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val serverModule = module {
    singleOf(::PostgresUserRepository) bind UserRepository::class
    singleOf(::AuthService)
}
