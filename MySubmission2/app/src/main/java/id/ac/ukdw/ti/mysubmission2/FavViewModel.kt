package id.ac.ukdw.ti.mysubmission2

import androidx.lifecycle.ViewModel

class FavViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    fun getHeadlineNews() = newsRepository.getHeadlineNews()
}