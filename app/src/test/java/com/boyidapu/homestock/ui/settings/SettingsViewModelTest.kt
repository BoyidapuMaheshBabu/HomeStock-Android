package com.boyidapu.homestock.ui.settings

import com.boyidapu.homestock.test.fake.FakeUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeUserRepository: FakeUserRepository
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeUserRepository = FakeUserRepository(initialName = "Priya", initialOnboardingCompleted = true)
        viewModel = SettingsViewModel(fakeUserRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun startEditing_populatesCurrentSavedName(): Unit = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        viewModel.startEditing()

        assertTrue(viewModel.uiState.value.isEditing)
        assertEquals("Priya", viewModel.uiState.value.nameInput)
    }

    @Test
    fun onNameInputChanged_over10Chars_truncatesTo10Chars(): Unit = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        viewModel.startEditing()
        viewModel.onNameInputChanged("PriyaMaheshBabu")

        assertEquals("PriyaMahes", viewModel.uiState.value.nameInput)
        assertEquals(10, viewModel.uiState.value.nameInput.length)
    }

    @Test
    fun saveName_blankInput_showsError(): Unit = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        viewModel.startEditing()
        viewModel.onNameInputChanged("  ")
        viewModel.saveName()

        assertEquals("Please enter a valid name", viewModel.uiState.value.errorMessage)
        assertTrue(viewModel.uiState.value.isEditing)
    }

    @Test
    fun saveName_validNewName_updatesSuccessfully(): Unit = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        viewModel.startEditing()
        viewModel.onNameInputChanged("Akka Priya")
        viewModel.saveName()

        assertFalse(viewModel.uiState.value.isEditing)
        assertEquals("Name updated successfully", viewModel.uiState.value.successMessage)
        assertEquals("Akka Priya", fakeUserRepository.userName.value)
    }
}
