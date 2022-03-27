package id.ac.ukdw.ti.mysubmission2.ui.setting

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import id.ac.ukdw.ti.mysubmission2.R
import id.ac.ukdw.ti.mysubmission2.ViewModelFactory
import id.ac.ukdw.ti.mysubmission2.ui.main.MainActivity
import id.ac.ukdw.ti.mysubmission2.ui.main.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class ThemeSettingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_setting)

        supportActionBar?.title = resources.getString(R.string.themeSetting)

        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)


        val mainViewModel = ViewModelProvider(this, ViewModelFactory(MainActivity.pref)).get(
            MainViewModel::class.java
        )
        mainViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }
    }
}