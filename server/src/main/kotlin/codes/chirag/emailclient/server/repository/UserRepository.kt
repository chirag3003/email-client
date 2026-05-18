package codes.chirag.emailclient.server.repository

import codes.chirag.emailclient.server.db.UserTable
import codes.chirag.emailclient.shared.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

interface UserRepository {
    suspend fun findByEmail(email: String): Pair<User, String>?
    suspend fun create(name: String, email: String, passwordHash: String): User
    suspend fun findById(id: Int): User?
}

class PostgresUserRepository : UserRepository {
    private fun resultRowToUser(row: ResultRow) = User(
        name = row[UserTable.name],
        email = row[UserTable.email],
        isAuthenticated = true
    )

    override suspend fun findByEmail(email: String): Pair<User, String>? = transaction {
        UserTable.select { UserTable.email eq email }
            .map { resultRowToUser(it) to it[UserTable.password] }
            .singleOrNull()
    }

    override suspend fun create(name: String, email: String, passwordHash: String): User = transaction {
        val insertStatement = UserTable.insert {
            it[UserTable.name] = name
            it[UserTable.email] = email
            it[UserTable.password] = passwordHash
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser) ?: User(name, email, true)
    }

    override suspend fun findById(id: Int): User? = transaction {
        UserTable.select { UserTable.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }
}
