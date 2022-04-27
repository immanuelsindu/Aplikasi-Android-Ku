package com.example.storyapp.ui.splash

import com.example.storyapp.data.StoryRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {
    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var splashViewModel: SplashViewModel
    private val dummyToken = "Token123"

    @Before
    fun setUp() {
        splashViewModel = SplashViewModel(storyRepository)
    }

    @Test
    fun `when Get Token Should Not Nul & Return String`() {
        val expectedToken = dummyToken

        `when`(splashViewModel.getTokenSession()).thenReturn(expectedToken)
        val actualToken = splashViewModel.getTokenSession()

        Assert.assertNotNull(actualToken)
        Assert.assertEquals(expectedToken, actualToken)
    }
}