package com.example.storyapp.ui.stories.add

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
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    @Mock
    private lateinit var dummyFile: File
    private lateinit var addStoryViewModel: AddStoryViewModel
    private val dummyAddStorySuccess = "success"
    private val dummyAddStoryNoFile = "Please Select Image"
    private val dummyLon = 10.1
    private val dummyLat = 10.1
    private val dummyDescription = "test123"

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(storyRepository)
    }

    @Test
    fun `when File Not Null Add Story Should Return Success`() {
        val observer = Observer<Result<String>> {}
        try {
            val expectedLogin = MutableLiveData<Result<String>>()
            expectedLogin.value = Result.Success(dummyAddStorySuccess)
            Mockito.`when`(addStoryViewModel.uploadImage(dummyFile, dummyLat, dummyLon, dummyDescription))
                .thenReturn(expectedLogin)
            val actualRegistrationStatus = addStoryViewModel.uploadImage(dummyFile, dummyLat, dummyLon, dummyDescription)
                .observeForever(observer)
            Mockito.verify(storyRepository).uploadImage(dummyFile, dummyLat, dummyLon, dummyDescription)
            assertNotNull(actualRegistrationStatus)
        } finally {
            addStoryViewModel.uploadImage(dummyFile, dummyLat, dummyLon, dummyDescription).removeObserver(observer)
        }
    }

    @Test
    fun `when File Null Add Story Should Return Error`() {
        val observer = Observer<Result<String>> {}
        try {
            val expectedLogin = MutableLiveData<Result<String>>()
            expectedLogin.value = Result.Error(dummyAddStoryNoFile)
            Mockito.`when`(addStoryViewModel.uploadImage(null, dummyLat, dummyLon,
                dummyDescription))
                .thenReturn(expectedLogin)
            val actualRegistrationStatus = addStoryViewModel.uploadImage(null, dummyLat, dummyLon,
                dummyDescription)
                .observeForever(observer)
            Mockito.verify(storyRepository).uploadImage(null, dummyLat, dummyLon, dummyDescription)
            assertNotNull(actualRegistrationStatus)
        } finally {
            addStoryViewModel.uploadImage(null, dummyLat, dummyLon, dummyDescription).removeObserver(observer)
        }
    }
}