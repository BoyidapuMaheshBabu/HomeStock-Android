package com.boyidapu.homestock.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userName: Flow<String>
    val isOnboardingCompleted: Flow<Boolean>
    suspend fun saveUserName(name: String)
}
