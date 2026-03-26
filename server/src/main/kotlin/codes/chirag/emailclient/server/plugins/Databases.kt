package codes.chirag.emailclient.server.plugins

import codes.chirag.emailclient.server.db.EmailTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val config = HikariConfig().apply {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = "jdbc:postgresql://localhost:5432/emailclient" // Adjust based on your environment
        username = "postgres"
        password = "password" // Adjust based on your environment
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    // Automatically create tables for ease of setup
    transaction {
        SchemaUtils.create(EmailTable)
    }
}
