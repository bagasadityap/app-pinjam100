package com.bagas.pinjam100.data.auth.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bagas.pinjam100.domain.model.auth.AuthSession
import com.bagas.pinjam100.domain.model.auth.AuthUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val AUTH_PREFERENCES_NAME = "auth_session"

private val Context.authPreferences: DataStore<Preferences> by preferencesDataStore(
    name = AUTH_PREFERENCES_NAME,
)

class AuthSessionLocalDataSource(context: Context) {

    private val dataStore = context.applicationContext.authPreferences

    fun observe(): Flow<AuthSession?> = dataStore.data
        .catch { throwable ->
            if (throwable is IOException) {
                emit(emptyPreferences())
            } else {
                throw throwable
            }
        }
        .map(::toSession)

    suspend fun save(session: AuthSession) {
        dataStore.edit { preferences ->
            preferences[Keys.ACCESS_TOKEN] = session.accessToken
            preferences[Keys.REFRESH_TOKEN] = session.refreshToken
            preferences[Keys.EXPIRES_AT] = session.expiresAtMillis

            session.user?.let {
                preferences[Keys.USER_ID] = it.id
                preferences[Keys.USER_CUSTOMER_NUMBER] = it.customerNumber
                preferences[Keys.USER_FULL_NAME] = it.fullName
                preferences[Keys.USER_PHONE_NUMBER] = it.phoneNumber
                preferences[Keys.USER_EMAIL] = it.email
                preferences[Keys.USER_PROFILE_COMPLETED] = it.profileCompleted
                preferences[Keys.USER_VERIFICATION_STATUS] = it.verificationStatus
            }
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private fun toSession(preferences: Preferences): AuthSession? {
        val id = preferences[Keys.USER_ID] ?: return null
        val customerNumber = preferences[Keys.USER_CUSTOMER_NUMBER] ?: return null
        val fullName = preferences[Keys.USER_FULL_NAME] ?: return null
        val phoneNumber = preferences[Keys.USER_PHONE_NUMBER] ?: return null
        val email = preferences[Keys.USER_EMAIL] ?: return null
        val profileCompleted = preferences[Keys.USER_PROFILE_COMPLETED] ?: return null
        val verificationStatus = preferences[Keys.USER_VERIFICATION_STATUS] ?: return null

        val accessToken = preferences[Keys.ACCESS_TOKEN] ?: return null
        val refreshToken = preferences[Keys.REFRESH_TOKEN] ?: return null
        val expiresAt = preferences[Keys.EXPIRES_AT] ?: return null

        return AuthSession(
            user = AuthUser(
                id = id,
                customerNumber = customerNumber,
                fullName = fullName,
                phoneNumber = phoneNumber,
                email = email,
                profileCompleted = profileCompleted,
                verificationStatus = verificationStatus
            ),
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresAtMillis = expiresAt,
        )
    }

    suspend fun currentSession(): AuthSession? = dataStore.data
        .catch { throwable ->
            if (throwable is IOException) {
                emit(emptyPreferences())
            } else {
                throw throwable
            }
        }
        .map(::toSession)
        .first()

    private object Keys {
        val USER_ID = stringPreferencesKey("user_id")
        val USER_CUSTOMER_NUMBER = stringPreferencesKey("user_customer_number")
        val USER_FULL_NAME = stringPreferencesKey("user_full_name")
        val USER_PHONE_NUMBER = stringPreferencesKey("user_phone_number")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PROFILE_COMPLETED = booleanPreferencesKey("user_profile_completed")
        val USER_VERIFICATION_STATUS = stringPreferencesKey("user_verification_status")

        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val EXPIRES_AT = longPreferencesKey("expires_at")
    }
}