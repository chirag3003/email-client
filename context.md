# Backend Context: Kotlin Ktor Email Client Server

## 1. Developer Background

- Expert in: JS/TS/Go
- Frameworks: Express, Hono, Gin, Go Fiber
- Kotlin/Ktor: complete beginner
- Learning goal: build Kotlin backends with Ktor from scratch

## 2. Project Overview

An authentication REST API built with Ktor (Kotlin's async HTTP framework). Three endpoints: POST /signup, POST /login, GET /me. Uses PostgreSQL via Exposed ORM, JWT authentication (HMAC256), Koin for dependency injection, kotlinx.serialization for JSON, and HOCON configuration with environment variable overrides. The server runs on Netty (non-blocking I/O engine). The architecture follows a layered pattern: Routes (HTTP handlers) → Services (business logic) → Repository (data access) → Database.

## 3. File Structure

```
server/
├── build.gradle.kts                        # Module build config: plugins, dependencies
├── src/main/
│   ├── resources/application.conf          # HOCON config: port, DB, JWT settings
│   └── kotlin/codes/chirag/emailclient/server/
│       ├── Main.kt                         # Entry point + app bootstrap + config helpers
│       ├── db/UserTable.kt                 # Exposed table schema definition
│       ├── di/ServerModule.kt              # Koin DI bindings (singletons)
│       ├── plugins/
│       │   ├── Databases.kt                # HikariCP pool + Exposed connection + auto-migration
│       │   ├── Routing.kt                  # Route registration hub
│       │   ├── Security.kt                 # JWT auth provider setup
│       │   └── Serialization.kt            # JSON content negotiation plugin
│       ├── repository/UserRepository.kt    # Repository interface + PostgresUserRepository impl
│       ├── routes/AuthRoutes.kt            # HTTP endpoints + request/response DTOs + token generation
│       └── services/AuthService.kt         # Business logic (signup, login, getUser)
└── src/test/
    └── kotlin/.../services/AuthServiceTest.kt  # Unit tests with MockK
```

Layer dependency graph: Main → Plugins → Routes → Services → Repository → Database. Each layer only depends on the layer below it. Routes know about Services, Services know about Repository, Repository knows about Exposed/Database. Nothing below knows about what's above.

## 4. Technology Stack

### 4.1 Ktor (HTTP Framework)

Kotlin's asynchronous HTTP framework built by JetBrains. It uses a plugin-based architecture where capabilities are added via `install(PluginName) { config }`. The `Application.module()` function serves as the app setup entry point, analogous to `app.js` in Express or `main.go` in Gin. Ktor is coroutine-based, meaning all request handlers are suspending functions that don't block threads. The framework provides routing DSL, request/response handling, content negotiation, authentication, and more through its plugin system.

### 4.2 Netty (HTTP Engine)

Netty is the underlying non-blocking I/O server that handles raw HTTP connections. It sits below Ktor and manages thread pools, socket connections, and HTTP protocol parsing. The `embeddedServer(Netty, module = ...)` pattern creates a self-contained server without needing an external servlet container. This is equivalent to Node's built-in `http` module or Go's `net/http` package. Ktor supports multiple engines (Netty, Jetty, CIO, Tomcat), but Netty is the most common choice.

### 4.3 Exposed (ORM)

JetBrains' SQL toolkit for Kotlin. It provides two flavors: DSL (type-safe SQL builders) and DAO (active record pattern). This project uses the DSL flavor. Tables are defined as Kotlin objects extending `Table`. Queries are built using chainable functions like `select`, `where`, `map`. All database operations are wrapped in `transaction { }` blocks which handle connection borrowing from the pool, commit/rollback, and isolation. Equivalent to Knex or Prisma in JS, or GORM in Go.

### 4.4 HikariCP (Connection Pool)

A high-performance JDBC connection pool. It maintains a pool of reusable database connections instead of opening a new connection per request. Configured via `HikariConfig` with driver class, JDBC URL, credentials, pool size, auto-commit setting, and transaction isolation level. The `HikariDataSource` wraps the config and implements `DataSource`. Equivalent to `pg.Pool` in Node.js or `sql.Open` with `SetMaxOpenConns` in Go.

### 4.5 Koin (Dependency Injection)

A lightweight dependency injection framework for Kotlin. Uses Kotlin DSL instead of annotation processing. A `module { }` block defines bindings: `singleOf(::ClassName)` registers a singleton, `bind Interface::class` registers it under an interface type. Dependencies are retrieved via `by inject<Type>()` delegation. Koin auto-resolves constructor dependencies — if `AuthService` takes `UserRepository` in its constructor, Koin injects the registered implementation automatically. Equivalent to Awilix in JS or Google Wire in Go.

### 4.6 kotlinx.serialization (JSON)

Kotlin's official serialization library. The `@Serializable` annotation on a data class triggers compiler plugin code generation for JSON encoding/decoding. Combined with Ktor's `ContentNegotiation` plugin, it automatically parses incoming JSON request bodies into Kotlin objects (`call.receive<T>()`) and serializes Kotlin objects into JSON responses (`call.respond(obj)`). Equivalent to Zod schemas in TS or struct tags in Go.

### 4.7 JWT (auth0 java-jwt)

The `com.auth0:java-jwt` library handles JWT creation and verification. `JWT.create()` returns a builder for token generation with methods like `.withAudience()`, `.withIssuer()`, `.withClaim()`, `.withExpiresAt()`, `.sign(Algorithm.HMAC256(secret))`. For verification, `JWT.require(algorithm).withAudience().withIssuer().build()` creates a verifier that validates signature, audience, and issuer. Equivalent to `jsonwebtoken` in JS or `golang-jwt` in Go.

### 4.8 HOCON (Config)

Human-Optimized Config Object Notation. A superset of JSON used by Ktor for configuration. The `application.conf` file uses HOCON syntax. Environment variable overrides use the `${?VAR_NAME}` syntax — if the env var exists, it overrides the default value. Config values are accessed via `config.property("path.to.value").getString()`. Extension functions on `ApplicationConfig` provide typed accessors. Equivalent to `.env` files with `dotenv` in JS or `viper` in Go.

## 5. Key Kotlin Concepts

### 5.1 Functions

`fun` declares a function. `suspend fun` declares an asynchronous function that can be paused and resumed without blocking the thread — Kotlin's coroutines handle the suspension. Suspending functions can only be called from other suspending functions or coroutine scopes. Extension functions add methods to existing types without inheritance: `fun Type.methodName()` adds `methodName` to `Type`. Lambdas use `{ params -> body }` syntax and are heavily used in DSLs (routing, transactions, config).

### 5.2 Variables

`val` declares an immutable reference (like `const` in JS). `var` declares a mutable reference (like `let` in JS). Type inference works for both: `val x = "hello"` infers `String`. Constructor parameters declared with `val` become properties: `class Foo(val bar: String)` makes `bar` a readable property on `Foo`.

### 5.3 Null Safety

Types are non-nullable by default. `String` cannot be null; `String?` can. The `?.` safe call operator returns null if the receiver is null (like `?.` in TS). The `?:` Elvis operator provides a default value or early return: `value ?: return@label` means "if null, return from this lambda." The `!!` operator forces non-null assertion and throws NullPointerException if null — avoid it. Destructuring nullable pairs: `val (user, hash) = result ?: return null`.

### 5.4 Classes

`data class` is an immutable data holder that auto-generates `equals()`, `hashCode()`, `toString()`, and `copy()`. Used for DTOs, models, and request/response objects. `object` declares a singleton — one instance exists for the entire application. Used for table definitions and module-level constants. `interface` defines a contract without implementation. `class(val prop: Type)` declares a class with a constructor parameter that becomes a property.

### 5.5 Collections

`List<T>`, `Set<T>`, `Map<K, V>` are the standard collections. Chainable operations: `.map { }`, `.filter { }`, `.singleOrNull()`, `.first()`, `.isEmpty()`. `Pair<A, B>` holds two values and supports destructuring: `val (first, second) = pair`. `to` infix function creates pairs: `"key" to "value"`.

### 5.6 Scope Functions

`apply { }` runs a block on an object and returns the object — used for builder/configuration patterns (like `HikariConfig().apply { ... }`). `let { }` runs a block with the object as `it` and returns the block's result — used for null-safe transformations. `also { }` runs a block for side effects and returns the original object.

### 5.7 Visibility

`public` (default) — visible everywhere. `private` — visible within the class/file. `internal` — visible within the module. `protected` — visible in subclasses. No `package-private` like Java.

## 6. Build System (Gradle)

### 6.1 File Roles

`settings.gradle.kts` — Project-level config: defines the project name, registers modules via `include(":moduleName")`, and declares where to download plugins and dependencies (repositories). This is read first by Gradle.

`Root build.gradle.kts` — Declares shared plugins with `apply false` so they're loaded into the classloader once and available to all modules without re-downloading.

`Module build.gradle.kts` — Module-specific config: applies plugins, declares dependencies, configures platform targets. Each module has its own.

`gradle.properties` — Key-value settings for Gradle and Kotlin compiler: JVM memory args (`-Xmx4096M`), caching flags, code style, Android-specific settings.

`gradle/libs.versions.toml` — Version catalog: centralized dependency version management. Three sections: `[versions]` for version numbers, `[libraries]` for dependencies (group:artifact with version.ref), `[plugins]` for Gradle plugins (ID with version.ref). Referenced as `libs.versions.X`, `libs.plugins.X`, `libs.X`.

`gradle/wrapper/` — Contains `gradle-wrapper.jar` (downloads Gradle) and `gradle-wrapper.properties` (which Gradle version to use). Ensures consistent Gradle versions across machines.

### 6.2 Dependency Scopes

`implementation` — available at compile time and runtime, not exposed to consumers. `testImplementation` — available only during test compilation and execution. `debugImplementation` — available only in debug builds. `api` — like implementation but exposed to consumers (not used in this project).

### 6.3 Version Catalog Syntax

In `libs.versions.toml`: `[versions]` section defines version strings. `[libraries]` section defines dependencies with `module = "group:artifact"` and `version.ref = "versionKey"`. `[plugins]` section defines plugins with `id = "plugin.id"` and `version.ref = "versionKey"`. In `build.gradle.kts`, reference them as `libs.plugins.pluginName`, `libs.library.name`, `libs.versions.name`.

### 6.4 Execution Flow

Running `./gradlew :server:run`: gradlew shell script reads `gradle/wrapper/gradle-wrapper.properties` to find the Gradle version (8.14.3). Downloads that Gradle version if not cached. Gradle JVM starts. Reads `settings.gradle.kts` — discovers modules (composeApp, shared, server) and repositories (google, mavenCentral, gradlePluginPortal). Reads root `build.gradle.kts` — loads plugins into classloader. Reads `gradle/libs.versions.toml` — resolves all version references. Reads target module `build.gradle.kts` — applies plugins, resolves dependencies from repositories. Executes the requested task.

## 7. Request Flow (End to End)

POST /signup with JSON body `{ "name": "Chirag", "email": "c@test.com", "passwordHash": "abc123" }`: Netty receives the raw HTTP request. ContentNegotiation plugin (installed in Serialization.kt) automatically parses the JSON body into a `SignupRequest` data class. Routing (configured in Routing.kt) matches the request to POST /signup. The handler in AuthRoutes.kt runs: `call.receive<SignupRequest>()` returns the parsed object. `inject<AuthService>()` retrieves the singleton AuthService from Koin's container (Koin auto-injected UserRepository into AuthService's constructor). `authService.signup()` is called — it first calls `userRepository.findByEmail()` which runs `SELECT * FROM users WHERE email = ?` inside a `transaction { }` block. If user exists, throws `BadRequestException` which Ktor converts to HTTP 400. If not, calls `userRepository.create()` which runs `INSERT INTO users (name, email, password) VALUES (?, ?, ?)`. Back in the route handler, `generateToken(email)` creates a JWT with the email claim, audience, issuer, and expiry. `call.respond(AuthResponse(token, user))` serializes the response to JSON via ContentNegotiation and sends HTTP 200.

POST /login: Same flow but `authService.login()` looks up the user, compares the stored password hash with the provided one, returns null if mismatch (handler responds 401), or returns the user if match.

GET /me (protected): The `authenticate("auth-jwt") { }` block triggers the JWT auth provider. The verifier validates signature, audience, issuer. The `validate` block checks the email claim exists. If valid, creates a `JWTPrincipal`. The handler extracts the email from `call.principal<JWTPrincipal>()`, calls `authService.getUser()`, responds with the User object or 404.

## 8. Configuration (application.conf)

HOCON structure with three top-level blocks. `ktor.deployment` — port (default 8080, override with `KTOR_PORT`), host (0.0.0.0). `ktor.application.modules` — fully qualified name of the entry point function (`codes.chirag.emailclient.server.ApplicationKt.module`). `jwt` — secret, audience, domain, realm, expiryMillis, all with env var overrides. `database` — jdbcUrl (PostgreSQL connection string), username, password, maximumPoolSize, all with env var overrides.

Env var override syntax: `property = "default"` followed by `property = ${?ENV_VAR}`. If the env var is set, it overrides the default. If not, the default is used.

Config values are read via `config.property("path.to.value").getString()`. Extension functions in Main.kt provide shorthand accessors: `fun ApplicationConfig.jwtSecret() = property("jwt.secret").getString()`. These are called as `config.jwtSecret()` throughout the codebase.

## 9. Database Layer

### Table Definition

Exposed tables are Kotlin singletons (`object`) extending `Table("tableName")`. Columns are defined as properties: `integer("id").autoIncrement()`, `varchar("name", 255)`, `varchar("email", 255).uniqueIndex()`. The primary key is overridden: `override val primaryKey = PrimaryKey(id)`. This generates SQL DDL: `CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL UNIQUE, password VARCHAR(255) NOT NULL)`.

### Repository Pattern

An interface defines the data access contract: `UserRepository` with methods `findByEmail`, `create`, `findById`. `PostgresUserRepository` implements it using Exposed DSL. Each method wraps queries in `transaction { }` which borrows a connection from HikariCP, executes the query, commits on success, rolls back on failure. Queries use DSL: `UserTable.select { UserTable.email eq email }` generates `SELECT * FROM users WHERE email = ?`. Results are mapped with `.map { resultRowToUser(it) }`. Inserts use `UserTable.insert { it[column] = value }`. `resultRowToUser` converts a `ResultRow` to the domain `User` model.

### Connection Setup

`HikariConfig` is configured with driver class name, JDBC URL, username, password, pool size, auto-commit disabled, and repeatable read isolation. `HikariDataSource` wraps the config. `Database.connect(dataSource)` tells Exposed to use this datasource. `SchemaUtils.create(UserTable)` auto-creates the table on startup if it doesn't exist (like running a migration).

## 10. Authentication

### Token Generation

In AuthRoutes.kt, `generateToken(email)` uses `JWT.create()` builder: `.withAudience(jwtAudience)` sets the audience claim, `.withIssuer(jwtDomain)` sets the issuer, `.withClaim("email", email)` adds a custom claim, `.withExpiresAt(Date(System.currentTimeMillis() + jwtExpiryMillis))` sets expiration (default 1 hour), `.sign(Algorithm.HMAC256(jwtSecret))` signs with HMAC256. Returns the compact JWT string.

### Token Verification

In Security.kt, `install(Authentication) { jwt("auth-jwt") { ... } }` registers a named JWT auth provider. `JWT.require(Algorithm.HMAC256(secret)).withAudience().withIssuer().build()` creates a verifier. The `validate` block receives a `JWTCredential` and returns `JWTPrincipal` if the email claim is non-empty, or `null` to reject. The `challenge` block runs on validation failure — responds 401.

### Route Protection

`authenticate("auth-jwt") { get("/me") { ... } }` wraps routes that require a valid JWT. Inside the block, `call.principal<JWTPrincipal>()` extracts the verified token. Claims are accessed via `principal.payload.getClaim("email").asString()`. Early returns use labeled return syntax: `return@get call.respond(HttpStatusCode.Unauthorized)`.

## 11. Testing

### Framework

`kotlin.test` provides assertions: `assertEquals(expected, actual)`, `assertNull(value)`, `assertFailsWith<ExceptionType> { block }`. `MockK` provides mocking: `mockk<UserRepository>()` creates a mock, `coEvery { mock.method() } returns value` stubs suspend functions, `coVerify { mock.method() }` verifies calls, `coVerify(exactly = 0) { mock.method(any()) }` verifies no calls. `runTest` from `kotlinx-coroutines-test` provides a test coroutine scope.

### Test Structure

`AuthServiceTest` has `@BeforeTest fun setup()` that creates fresh mocks and service instance before each test. Each `@Test` function tests one scenario. The pattern is: Given (coEvery stubs) → When (call the method under test) → Then (assert result + coVerify interactions). Tests cover: signup happy path, signup duplicate email (BadRequestException), login success, login wrong password (null), login unknown user (null), getUser found, getUser not found.

## 12. What's NOT Implemented (Build Next)

OAuth 2.0 flows — no Google or Zoho login integration. No OAuth authorization code flow, no provider token exchange, no refresh token handling. Email provider API integration — no Gmail API client, no IMAP library, no HTTP client for external email services. Email sync engine — no scheduled polling, no background jobs, no webhook receivers for push notifications. Real password hashing — the server stores passwords as-is from the client (client sends pre-hashed, server does not hash again with bcrypt/argon2). WebSocket for real-time updates — no bidirectional communication for live email updates. Rate limiting — no request throttling or abuse prevention. Input validation — no email format validation, no password strength requirements, no request body validation beyond serialization. Refresh token rotation — only access tokens exist, no refresh token mechanism. CORS configuration — no cross-origin resource sharing setup for browser clients. Logging middleware — no request/response logging, no structured logging beyond Logback basics. Database migrations — currently uses `SchemaUtils.create()` which only creates tables, no versioned migration system. Health check endpoint — no `/health` or `/ready` endpoint for monitoring.
