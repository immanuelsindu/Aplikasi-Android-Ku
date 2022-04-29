package id.ac.ukdw.sub1_intermediate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ac.ukdw.sub1_intermediate.userSession.UserPreferencesDS

//class ViewModelFactoryDS(private val pref: UserPreferencesDS) : ViewModelProvider.NewInstanceFactory(){
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(UserViewModelDS::class.java)) {
//            return UserViewModelDS(pref) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
//    }
//}