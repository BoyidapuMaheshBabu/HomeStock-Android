package com.boyidapu.homestock.data.repository

import com.boyidapu.homestock.data.preferences.UserPreferences
import com.boyidapu.homestock.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userPreferences: UserPreferences
) : UserRepository {

    override val userName: Flow<String> = userPreferences.userName

    override val isOnboardingCompleted: Flow<Boolean> = userPreferences.isOnboardingCompleted

    override suspend fun saveUserName(name: String) {
        userPreferences.saveUserName(name)
    }
}
