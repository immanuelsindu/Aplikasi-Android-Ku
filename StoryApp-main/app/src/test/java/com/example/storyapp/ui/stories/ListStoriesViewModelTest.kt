package com.example.storyapp.ui.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.remote.response.ListStoryItem
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ListStoriesViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var dummyPagingData: PagingData<ListStoryItem>
    private lateinit var listStoriesViewModel: ListStoriesViewModel

    @Before
    fun setUp() {
        listStoriesViewModel = ListStoriesViewModel(storyRepository)
    }

    @Test
    fun `when Find Stories Should Not Null and Return PagingData of ListStoryItem`() {
        val observer = Observer<PagingData<ListStoryItem>> {}
        try {
            val expectedData = MutableLiveData<PagingData<ListStoryItem>>()
            expectedData.value = dummyPagingData
            Mockito.`when`(listStoriesViewModel.findStories())
                .thenReturn(expectedData)
            val actualRegistrationStatus =
                listStoriesViewModel.findStories().observeForever(observer)
            Mockito.verify(storyRepository).getPagingStories()
            assertNotNull(actualRegistrationStatus)
        } finally {
            listStoriesViewModel.findStories().removeObserver(observer)
        }
    }
}