package com.bagas.pinjam100.security

import android.content.Context

class SecurityManager(
    private val context: Context
) {

    fun checkDevice(): SecurityState {
        return if (RootDetector.isRooted(context)) {
            SecurityState.Rooted
        } else {
            SecurityState.Secure
        }
    }
}