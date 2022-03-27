package id.ac.ukdw.ti.mysubmission2

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ac.ukdw.ti.mysubmission2.ui.main.MainActivity.Companion.pref

import id.ac.ukdw.ti.mysubmission2.ui.main.MainViewModel
import id.ac.ukdw.ti.mysubmission2.ui.setting.SettingPreferences

class ViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}