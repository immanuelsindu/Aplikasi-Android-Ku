package id.ac.ukdw.sub1_intermediate

import android.content.Context

internal class UserPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val ID = "id"
        private const val TOKEN = "token"
    }
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun setUser(value: UserModel) {
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(ID, value.id)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }
    fun getUser(): UserModel {
        val model = UserModel()
        model.name = preferences.getString(NAME, "")
        model.id = preferences.getString(ID,"")
        model.token = preferences.getString(TOKEN,"")
        return model
    }
    fun clearUserSession(){
        preferences.edit().clear().commit()
    }
}