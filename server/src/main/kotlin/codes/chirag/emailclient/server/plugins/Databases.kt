package codes.chirag.emailclient.server.plugins

import codes.chirag.emailclient.server.db.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val config = environment.config

    val hikariConfig = HikariConfig().apply {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = config.property("database.jdbcUrl").getString()
        username = config.property("database.username").getString()
        password = config.property("database.password").getString()
        maximumPoolSize = config.property("database.maximumPoolSize").getString().toInt()
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }
    val dataSource = HikariDataSource(hikariConfig)
    Database.connect(dataSource)

    transaction {
        SchemaUtils.create(UserTable)
    }
}
