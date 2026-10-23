package com.boyidapu.homestock.ui.home

import com.boyidapu.homestock.test.fake.FakeTransactionRepository
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
class HomeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeUserRepository: FakeUserRepository
    private lateinit var fakeTransactionRepository: FakeTransactionRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeUserRepository = FakeUserRepository(initialName = "Priya", initialOnboardingCompleted = true)
        fakeTransactionRepository = FakeTransactionRepository()
        viewModel = HomeViewModel(fakeUserRepository, fakeTransactionRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiState_combinesUserNameAndMonthlySpending(): Unit = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        fakeTransactionRepository.setSpendingPaise(50000L) // ₹500.00

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.greeting.contains("Priya"))
        assertEquals(50000L, state.thisMonthSpendingPaise)
    }
}
