package id.ac.ukdw.sub1_intermediate

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.ac.ukdw.sub1_intermediate.userSession.UserPreferencesDS
import kotlinx.coroutines.launch

//class UserViewModelDS(private val pref: UserPreferencesDS) : ViewModel() {
//    // ini buat UserViewModel ke DataStore
//    fun getCurrentToken(): String {
//
////        return pref.getCurrenctToken().toString()
//    }
//
//    fun saveCurrentToken(userToken: String) {
//        viewModelScope.launch {
//            pref.saveCurrentToken(userToken)
//        }
//    }

    //    private lateinit var mUserSession: MutableLiveData<UserModel?>
//
//    fun getUser(): LiveData<UserModel?> {
//        mUserSession = MutableLiveData()
//        return mUserSession
//    }

//}