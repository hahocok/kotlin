package com.android.kotlin.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.android.kotlin.data.Repository
import com.android.kotlin.data.model.Note
import com.android.kotlin.data.model.Result
import com.android.kotlin.ui.main.MainViewModel
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: Repository = mockk()
    private val notesLiveData = MutableLiveData<Result>()

    private lateinit var viewModel: MainViewModel


    @Before
    fun setup() {
        clearMocks(mockRepository)
        every { mockRepository.getNotes() } returns notesLiveData
        viewModel = MainViewModel(mockRepository)
    }


    @Test
    fun `should call getNotes`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `should return notes`() {
        var result: List<Note>? = null
        val testData = listOf(Note("1"), Note("2"))
        viewModel.getViewState().observeForever {
            result = it.data
        }
        notesLiveData.value = Result.Success(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.getViewState().observeForever {
            result = it?.error
        }
        notesLiveData.value = Result.Error(error = testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should remove observer`() {
        viewModel.onCleared()
        assertFalse(notesLiveData.hasObservers())
    }
}