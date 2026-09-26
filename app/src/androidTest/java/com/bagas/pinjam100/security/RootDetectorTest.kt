package com.bagas.pinjam100.security

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class RootDetectorTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun isRootedShouldReturnFalseWhenDeviceIsNotRooted() {
        val actualResult = RootDetector.isRooted(context)

        assertFalse(actualResult)
    }
}