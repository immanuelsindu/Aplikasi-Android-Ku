package id.ac.ukdw.sub1_intermediate

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ac.ukdw.sub1_intermediate.homeStory.StoryViewModel

class ViewModelFactory_Paging(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}