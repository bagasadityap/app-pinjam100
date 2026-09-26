package com.bagas.pinjam100.security

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SecurityManagerTest {

    private lateinit var context: Context
    private lateinit var securityManager: SecurityManager

    @Before
    fun setUp() {
        context = mockk()
        securityManager = SecurityManager(context)

        mockkObject(RootDetector)
    }

    @After
    fun tearDown() {
        unmockkObject(RootDetector)
    }

    @Test
    fun `checkDevice should return Rooted when device is rooted`() {

        every {
            RootDetector.isRooted(context)
        } returns true

        val actualResult = securityManager.checkDevice()

        assertEquals(
            SecurityState.Rooted,
            actualResult
        )

        verify(exactly = 1) {
            RootDetector.isRooted(context)
        }
    }

    @Test
    fun `checkDevice should return Secure when device is not rooted`() {

        every {
            RootDetector.isRooted(context)
        } returns false

        val actualResult = securityManager.checkDevice()

        assertEquals(
            SecurityState.Secure,
            actualResult
        )

        verify(exactly = 1) {
            RootDetector.isRooted(context)
        }
    }
}