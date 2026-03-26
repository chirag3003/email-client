package codes.chirag.emailclient.server.di

import codes.chirag.emailclient.server.repository.EmailRepository
import codes.chirag.emailclient.server.repository.PostgresEmailRepository
import codes.chirag.emailclient.server.services.EmailService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val serverModule = module {
    singleOf(::PostgresEmailRepository) bind EmailRepository::class
    singleOf(::EmailService)
}
