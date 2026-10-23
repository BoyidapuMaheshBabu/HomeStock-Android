package com.boyidapu.homestock.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boyidapu.homestock.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val currentSavedName: String = "",
    val nameInput: String = "",
    val isEditing: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isSaving: Boolean = false
)

class SettingsViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _editState = MutableStateFlow(SettingsUiState())

    val uiState: StateFlow<SettingsUiState> = combine(
        userRepository.userName,
        _editState
    ) { savedName, editState ->
        editState.copy(
            currentSavedName = savedName,
            nameInput = if (!editState.isEditing) savedName else editState.nameInput
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun startEditing() {
        val currentName = uiState.value.currentSavedName
        _editState.update {
            it.copy(
                isEditing = true,
                nameInput = currentName,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun cancelEditing() {
        _editState.update {
            it.copy(
                isEditing = false,
                nameInput = "",
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun onNameInputChanged(input: String) {
        _editState.update {
            it.copy(
                nameInput = input,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun saveName() {
        val trimmed = _editState.value.nameInput.trim()
        if (trimmed.isEmpty()) {
            _editState.update {
                it.copy(errorMessage = "Please enter a valid name")
            }
            return
        }

        viewModelScope.launch {
            _editState.update { it.copy(isSaving = true) }
            try {
                userRepository.saveUserName(trimmed)
                _editState.update {
                    it.copy(
                        isEditing = false,
                        isSaving = false,
                        successMessage = "Name updated successfully",
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _editState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "Failed to update name"
                    )
                }
            }
        }
    }
}
