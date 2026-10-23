package com.boyidapu.homestock.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boyidapu.homestock.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WelcomeUiState(
    val nameInput: String = "",
    val errorMessage: String? = null,
    val isSaving: Boolean = false,
    val isSaveSuccess: Boolean = false
)

class WelcomeViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    companion object {
        const val MAX_USER_NAME_LENGTH = 10
    }

    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    fun onNameInputChanged(input: String) {
        val truncated = if (input.length > MAX_USER_NAME_LENGTH) input.take(MAX_USER_NAME_LENGTH) else input
        _uiState.update {
            it.copy(
                nameInput = truncated,
                errorMessage = null
            )
        }
    }

    fun submitName() {
        val trimmedName = _uiState.value.nameInput.trim()
        if (trimmedName.isEmpty()) {
            _uiState.update {
                it.copy(errorMessage = "Please enter your name")
            }
            return
        }

        if (trimmedName.length > MAX_USER_NAME_LENGTH) {
            _uiState.update {
                it.copy(errorMessage = "Name must be at most $MAX_USER_NAME_LENGTH characters")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            try {
                userRepository.saveUserName(trimmedName.take(MAX_USER_NAME_LENGTH))
                _uiState.update {
                    it.copy(isSaving = false, isSaveSuccess = true)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "Failed to save name. Please try again."
                    )
                }
            }
        }
    }
}
