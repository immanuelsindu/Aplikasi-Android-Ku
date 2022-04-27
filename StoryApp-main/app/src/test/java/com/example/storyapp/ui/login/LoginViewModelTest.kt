package com.example.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.storyapp.data.StoryRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import com.example.storyapp.data.Result
import org.junit.Rule
import org.mockito.Mockito

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginStatus = true
    private val dummyEmail = "test@gmail.com"
    private val dummyPassword = "123456"

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when Login Should Not Null and Return Success`() {
        val observer = Observer<Result<Boolean>> {}
        try {
            val expectedLogin = MutableLiveData<Result<Boolean>>()
            expectedLogin.value = Result.Success(dummyLoginStatus)
            `when` (loginViewModel.login(dummyEmail, dummyPassword)).thenReturn(expectedLogin)
            val actualLoginStatus = loginViewModel.login(dummyEmail, dummyPassword)
                .observeForever(observer)
            Mockito.verify(storyRepository).login(dummyEmail, dummyPassword)
            assertNotNull(actualLoginStatus)
        } finally {
            loginViewModel.login(dummyEmail, dummyPassword).removeObserver(observer)
        }
    }
}