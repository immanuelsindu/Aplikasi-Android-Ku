package com.example.storyapp.ui.registration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.storyapp.data.Result
import com.example.storyapp.data.StoryRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegistrationViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var registrationViewModel: RegistrationViewModel
    private val dummyRegistrationStatus = "success"
    private val dummyName = "test123"
    private val dummyEmail = "test@gmail.com"
    private val dummyPassword = "123456"

    @Before
    fun setUp() {
        registrationViewModel = RegistrationViewModel(storyRepository)
    }

    @Test
    fun `when Registration Should Not Null and Return Success`() {
        val observer = Observer<Result<String>> {}
        try {
            val expectedLogin = MutableLiveData<Result<String>>()
            expectedLogin.value = Result.Success(dummyRegistrationStatus)
            Mockito.`when`(registrationViewModel.registration(dummyName, dummyEmail, dummyPassword))
                .thenReturn(expectedLogin)
            val actualRegistrationStatus = registrationViewModel.registration(dummyName,
                dummyEmail,
                dummyPassword)
                .observeForever(observer)
            Mockito.verify(storyRepository).registration(dummyName, dummyEmail, dummyPassword)
            assertNotNull(actualRegistrationStatus)
        } finally {
            registrationViewModel.registration(dummyName, dummyEmail, dummyPassword).removeObserver(observer)
        }
    }
}