package id.ac.ukdw.sub1_intermediate.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import id.ac.ukdw.sub1_intermediate.R
import id.ac.ukdw.sub1_intermediate.UserPreference
//import id.ac.ukdw.sub1_intermediate.UserViewModelDS
//import id.ac.ukdw.sub1_intermediate.ViewModelFactoryDS
import id.ac.ukdw.sub1_intermediate.databinding.ActivityMainBinding
import id.ac.ukdw.sub1_intermediate.homeStory.HomeStoryActivity
import id.ac.ukdw.sub1_intermediate.homeStory.UserModel
import id.ac.ukdw.sub1_intermediate.login.LoginActivity

import id.ac.ukdw.sub1_intermediate.register.RegisterActivity
import id.ac.ukdw.sub1_intermediate.userSession.UserPreferencesDS
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class MainActivity : AppCompatActivity() {
    companion object{
        private const val NAME = "name"
        private const val ASTRONOTIMAGE = "astronotImage"
        private const val DURATION = 450.toLong()
    }

    private lateinit var token: String
    private lateinit var mUserPreference: UserPreference
    private lateinit var userModel: UserModel
    private lateinit var astronotImage: ImageView
    private lateinit var binding: ActivityMainBinding
//    private lateinit var userVM: UserViewModelDS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        lifecycleScope.launch{
            checkSession()
        }

        //inisialisasi datastore


        astronotImage = findViewById(R.id.imageView)
        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, astronotImage, ASTRONOTIMAGE
                )
            startActivity(intent, optionsCompat.toBundle())
        }

        binding.buttonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, astronotImage, ASTRONOTIMAGE
                )
            startActivity(intent, optionsCompat.toBundle())
        }

        binding.tvLoginAsGuest.setOnClickListener {
            val intent = Intent(this, HomeStoryActivity::class.java)
            Toast.makeText(
                this@MainActivity,
                resources.getString(R.string.loginSuccessfully),
                Toast.LENGTH_SHORT
            ).show()
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            )
        }
        playAnimation()

    }

    private suspend fun checkSession(){
        val pref = UserPreferencesDS.getInstance(dataStore)

        Log.d("MainActivity","Ini merupakan token gan = "+ pref.getCurrenctToken())
        if(pref.getCurrenctName() != ""){
            val intent = Intent(this, HomeStoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
            intent.putExtra(NAME, pref.getCurrenctName())
            intent.putExtra("token", pref.getCurrenctToken())
            startActivity(intent)
        }
//        if (token != "") {
//            Log.d("MainActivity", "Ini adalah token = $token")
//            val intent = Intent(this, HomeStoryActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
//            intent.putExtra(NAME, "Percobaan")
//            startActivity(intent)
//        } else {
//            Log.d("MainActivity", "Ini adalah token = $token")
//        }
    }


//        val UserVMDS= ViewModelProvider(this, ViewModelFactoryDS(pref)).get(
//            UserViewModelDS::class.java
//        )

//        val token = UserVMDS.getCurrentToken()
//        if(token != "id.ac.ukdw.sub1_intermediate.userSession.UserPreferencesDS$"+"getCurrenctToken$"+"$"+"inlined"+"$"+"map$1@5371628"){
//            Log.d("MainActivity", "Ini adalah token = $token")
//            val intent = Intent(this, HomeStoryActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
//            intent.putExtra(NAME, "Percobaan")
//            startActivity(intent)
//        }

        // ganti bagian ini ke viewModel
//        mUserPreference = UserPreference(this)
//
//        userModel = mUserPreference.getUser()
//        when{
//            userModel.name.toString() != "" ->{
//                val intent = Intent(this, HomeStoryActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
//                intent.putExtra(NAME, userModel.name.toString())
//                startActivity(intent)
//            }
//        }

        //ke viewModel
//        userVM = ViewModelProvider(this)[UserViewModel::class.java]
//        val userModel = userVM.getUser()
//        if(userModel?.name != null){
//            val intent = Intent(this, HomeStoryActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
//                intent.putExtra(NAME, userModel.name.toString())
//                startActivity(intent)





    @SuppressLint("RestrictedApi")
    private fun playAnimation() {

        val logo = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1F).setDuration(DURATION)
        val buttonLogin = ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1F).setDuration(DURATION)
        val buttonRegister = ObjectAnimator.ofFloat(binding.buttonLogin, View.ALPHA, 1F).setDuration(DURATION)
        val textWelcome = ObjectAnimator.ofFloat(binding.textViewWelcome, View.ALPHA, 1F).setDuration(DURATION)
        val txtLoginGuest = ObjectAnimator.ofFloat(binding.tvLoginAsGuest, View.ALPHA, 1F).setDuration(DURATION)

        val together = AnimatorSet().apply {
            playTogether(buttonLogin, buttonRegister)
        }

        AnimatorSet().apply{
            playSequentially(logo, textWelcome, together, txtLoginGuest)
            start()
        }
    }
}