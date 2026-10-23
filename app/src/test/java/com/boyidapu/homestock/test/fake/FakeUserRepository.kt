package com.boyidapu.homestock.test.fake

import com.boyidapu.homestock.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeUserRepository(
    initialName: String = "",
    initialOnboardingCompleted: Boolean = false
) : UserRepository {

    private val _userName = MutableStateFlow(initialName)
    override val userName: StateFlow<String> = _userName.asStateFlow()

    private val _isOnboardingCompleted = MutableStateFlow(initialOnboardingCompleted)
    override val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted.asStateFlow()

    var shouldThrow = false

    override suspend fun saveUserName(name: String) {
        if (shouldThrow) {
            throw RuntimeException("Database error")
        }
        val trimmed = name.trim()
        if (trimmed.isNotEmpty()) {
            _userName.value = trimmed
            _isOnboardingCompleted.value = true
        }
    }
}
