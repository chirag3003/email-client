package codes.chirag.emailclient.server.services

import codes.chirag.emailclient.server.repository.UserRepository
import codes.chirag.emailclient.shared.model.User
import io.ktor.server.plugins.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class AuthServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var authService: AuthService

    @BeforeTest
    fun setup() {
        userRepository = mockk()
        authService = AuthService(userRepository)
    }

    @Test
    fun `signup creates new user when email does not exist`() = runTest {
        val name = "Chirag"
        val email = "chirag@example.com"
        val passwordHash = "hashed123"
        val expectedUser = User(name = name, email = email, isAuthenticated = true)

        coEvery { userRepository.findByEmail(email) } returns null
        coEvery { userRepository.create(name, email, passwordHash) } returns expectedUser

        val result = authService.signup(name, email, passwordHash)

        assertEquals(expectedUser, result)
        coVerify { userRepository.findByEmail(email) }
        coVerify { userRepository.create(name, email, passwordHash) }
    }

    @Test
    fun `signup throws BadRequestException when user already exists`() = runTest {
        val name = "Chirag"
        val email = "chirag@example.com"
        val passwordHash = "hashed123"
        val existingUser = User(name = name, email = email, isAuthenticated = true)

        coEvery { userRepository.findByEmail(email) } returns (existingUser to passwordHash)

        assertFailsWith<BadRequestException> {
            authService.signup(name, email, passwordHash)
        }

        coVerify { userRepository.findByEmail(email) }
        coVerify(exactly = 0) { userRepository.create(any(), any(), any()) }
    }

    @Test
    fun `login returns user when credentials match`() = runTest {
        val email = "chirag@example.com"
        val passwordHash = "hashed123"
        val expectedUser = User(name = "Chirag", email = email, isAuthenticated = true)

        coEvery { userRepository.findByEmail(email) } returns (expectedUser to passwordHash)

        val result = authService.login(email, passwordHash)

        assertEquals(expectedUser, result)
    }

    @Test
    fun `login returns null when user not found`() = runTest {
        val email = "nonexistent@example.com"

        coEvery { userRepository.findByEmail(email) } returns null

        val result = authService.login(email, "anyhash")

        assertNull(result)
    }

    @Test
    fun `login returns null when password does not match`() = runTest {
        val email = "chirag@example.com"
        val storedHash = "correct_hash"
        val wrongHash = "wrong_hash"
        val user = User(name = "Chirag", email = email, isAuthenticated = true)

        coEvery { userRepository.findByEmail(email) } returns (user to storedHash)

        val result = authService.login(email, wrongHash)

        assertNull(result)
    }

    @Test
    fun `getUser returns user when found`() = runTest {
        val email = "chirag@example.com"
        val expectedUser = User(name = "Chirag", email = email, isAuthenticated = true)

        coEvery { userRepository.findByEmail(email) } returns (expectedUser to "hash")

        val result = authService.getUser(email)

        assertEquals(expectedUser, result)
    }

    @Test
    fun `getUser returns null when not found`() = runTest {
        val email = "nonexistent@example.com"

        coEvery { userRepository.findByEmail(email) } returns null

        val result = authService.getUser(email)

        assertNull(result)
    }
}
