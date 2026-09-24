package com.bagas.pinjam100.data.auth.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.data.auth.local.AuthSessionLocalDataSource
import com.bagas.pinjam100.data.auth.remote.AuthApi
import com.bagas.pinjam100.domain.model.auth.AuthSession
import com.bagas.pinjam100.domain.model.auth.LoginCredentials
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private lateinit var localDataSource: AuthSessionLocalDataSource
    private lateinit var remoteDataSource: AuthApi
    private lateinit var json: Json
    private lateinit var repository: AuthRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()
    private val mockTime = 1700000000000L

    @Before
    fun setUp() {
        localDataSource = mockk(relaxed = true)
        remoteDataSource = mockk(relaxed = true)
        json = mockk(relaxed = true)

        repository = AuthRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            json = json,
            ioDispatcher = testDispatcher,
            clock = { mockTime }
        )
    }

    @Test
    fun `login should call remote api`() {
        runTest {
            val credentials = mockk<LoginCredentials>(relaxed = true)

            repository.login(credentials)

            coVerify(exactly = 1) {
                remoteDataSource.login(any())
            }
        }
    }

    @Test
    fun `logout should call remote api and clear local data when refresh token is present`() {
        runTest {
            val mockSession = mockk<AuthSession> {
                every { refreshToken } returns "valid_token"
            }
            coEvery { localDataSource.currentSession() } returns mockSession

            val result = repository.logout()

            assertTrue(result is AppResult.Success)
            coVerify(exactly = 1) { remoteDataSource.logout(any()) }
            coVerify(exactly = 1) { localDataSource.clear() }
        }
    }

    @Test
    fun `logout should ONLY clear local data and NOT call remote api when refresh token is empty`() {
        runTest {
            val mockSession = mockk<AuthSession> {
                every { refreshToken } returns ""
            }
            coEvery { localDataSource.currentSession() } returns mockSession

            val result = repository.logout()

            assertTrue(result is AppResult.Success)
            coVerify(exactly = 0) { remoteDataSource.logout(any()) }
            coVerify(exactly = 1) { localDataSource.clear() }
        }
    }

    @Test
    fun `deleteAccount should call remote api and clear local data`() {
        runTest {
            repository.deleteAccount()

            coVerify(exactly = 1) { remoteDataSource.deleteAccount() }
            coVerify(exactly = 1) { localDataSource.clear() }
        }
    }

    @Test
    fun `observeSession should emit session when it is NOT expired`() {
        runTest {
            val mockSession = mockk<AuthSession> {
                every { isExpiredAt(mockTime) } returns false
            }
            every { localDataSource.observe() } returns flowOf(mockSession)

            val result = repository.observeSession().first()

            assertEquals(mockSession, result)
        }
    }

    @Test
    fun `observeSession should emit null when session is expired`() {
        runTest {
            val mockSession = mockk<AuthSession> {
                every { isExpiredAt(mockTime) } returns true
            }
            every { localDataSource.observe() } returns flowOf(mockSession)

            val result = repository.observeSession().first()

            assertNull(result)
        }
    }
}