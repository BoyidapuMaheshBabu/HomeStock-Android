package com.boyidapu.homestock.ui.onboarding

import com.boyidapu.homestock.test.fake.FakeUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeUserRepository: FakeUserRepository
    private lateinit var viewModel: WelcomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeUserRepository = FakeUserRepository()
        viewModel = WelcomeViewModel(fakeUserRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun submitName_blankName_showsErrorMessage() {
        viewModel.onNameInputChanged("   ")
        viewModel.submitName()

        assertEquals("Please enter your name", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isSaveSuccess)
    }

    @Test
    fun submitName_validName_trimsAndSavesSuccessfully(): Unit = runTest {
        viewModel.onNameInputChanged("  Priya  ")
        viewModel.submitName()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.errorMessage)
        assertTrue(viewModel.uiState.value.isSaveSuccess)
        assertEquals("Priya", fakeUserRepository.userName.value)
        assertTrue(fakeUserRepository.isOnboardingCompleted.value)
    }

    @Test
    fun submitName_repositoryError_showsErrorAndDoesNotComplete(): Unit = runTest {
        fakeUserRepository.shouldThrow = true
        viewModel.onNameInputChanged("Priya")
        viewModel.submitName()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Failed to save name. Please try again.", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isSaveSuccess)
        assertFalse(fakeUserRepository.isOnboardingCompleted.value)
    }
}
