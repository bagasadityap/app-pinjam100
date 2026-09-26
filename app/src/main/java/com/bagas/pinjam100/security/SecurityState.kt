package com.bagas.pinjam100.security

sealed interface SecurityState {

    data object Checking : SecurityState

    data object Secure : SecurityState

    data object Rooted : SecurityState
}