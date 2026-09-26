package com.bagas.pinjam100.security

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.io.File

object RootDetector {

    fun isRooted(context: Context): Boolean {
        return checkTestKeys() ||
                checkSuBinary() ||
                checkSuCommand() ||
                checkRootManagementApps(context)
    }

    private fun checkTestKeys(): Boolean {
        return Build.TAGS?.contains("test-keys") == true
    }

    private fun checkSuBinary(): Boolean {
        val suPaths = arrayOf(
            "/system/bin/su",
            "/system/xbin/su",
            "/sbin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/data/local/su",
            "/su/bin/su"
        )

        return suPaths.any { path ->
            File(path).exists()
        }
    }

    private fun checkSuCommand(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(
                arrayOf("su", "-c", "id")
            )

            val exitCode = process.waitFor()

            exitCode == 0
        } catch (_: Exception) {
            false
        }
    }

    private fun checkRootManagementApps(
        context: Context
    ): Boolean {

        val packages = listOf(
            "com.topjohnwu.magisk",
            "eu.chainfire.supersu",
            "com.noshufou.android.su",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "com.yellowes.su"
        )

        val packageManager = context.packageManager

        return packages.any { packageName ->
            try {
                packageManager.getApplicationInfo(
                    packageName,
                    0
                )

                true
            } catch (_: PackageManager.NameNotFoundException) {
                false
            }
        }
    }
}