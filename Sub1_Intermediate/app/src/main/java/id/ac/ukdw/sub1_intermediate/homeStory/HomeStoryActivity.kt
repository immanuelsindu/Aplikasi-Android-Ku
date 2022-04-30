package id.ac.ukdw.sub1_intermediate.homeStory

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.ukdw.sub1_intermediate.LoadingStateAdapter
import id.ac.ukdw.sub1_intermediate.MyMaps
import id.ac.ukdw.sub1_intermediate.R
import id.ac.ukdw.sub1_intermediate.UserPreference
import id.ac.ukdw.sub1_intermediate.databinding.ActivityHomeStoryBinding
import id.ac.ukdw.sub1_intermediate.main.MainActivity
import id.ac.ukdw.sub1_intermediate.newStory.NewStoryActivity
import id.ac.ukdw.sub1_intermediate.userSession.UserPreferencesDS
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class HomeStoryActivity : AppCompatActivity() {
    companion object {
        private const val BEARER = "Bearer "
        private const val TOKEN = "token"
        private const val DURATION = 450.toLong()
        private const val DURATION2 = 1250.toLong()
        private const val NAME = "name"
    }
    private lateinit var binding: ActivityHomeStoryBinding
//    private lateinit var rcyStory: RecyclerView
    private lateinit var storyViewModel: StoryViewModel

//    private val pref = UserPreferencesDS.getInstance(dataStore)
//    private  var tokenGlobal: String? = "kosong"

//    private lateinit var userVM: UserViewModelDS
//    private lateinit var storyViewModel: StoryViewModel

//        val pref = UserPreferencesDS.getInstance(dataStore)
//        val UserVMDS= ViewModelProvider(this, ViewModelFactory("",this)).get(
//            StoryViewModel::class.java
//        )
//
//    private val storyViewModel: StoryViewModel by viewModels {
//        ViewModelFactory(this)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        supportActionBar?.title = resources.getString(R.string.titleStoryActivity)
//        playAnimation()
        showLoading(false)

//        mUserPreference = UserPreference(this)
//        rcyStory = findViewById(R.id.rcyStory)
        binding.rcyStory.layoutManager = LinearLayoutManager(this)

        val name = intent.getStringExtra("name")
        val iToken = intent.getStringExtra("token").toString()

//        val pref = UserPreferencesDS.getInstance(dataStore)
        storyViewModel= ViewModelProvider(this, ViewModelFactory(iToken, this))[StoryViewModel::class.java]


//        lifecycleScope.launch{
//            tokenGlobal = pref.getCurrenctToken()
//
//        }
//        val pref = UserPreferencesDS.getInstance(dataStore)
//        val name = mUserPreference.getUserName()


        if (name != "" && name != null) {
            supportActionBar?.title = resources.getString(R.string.welcome_home, name)
            val token = BEARER + iToken
//            Log.d("HomeStoryActivity", "Ini merupakan token di Home = $token")
//            getAllStory(token)
            getData()
        } else {
            supportActionBar?.title = resources.getString(R.string.welcomeGuest)
        }

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, NewStoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(TOKEN, iToken)
            intent.putExtra(NAME, name)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val iToken = intent.getStringExtra(TOKEN).toString()
        return when (item.itemId) {
            R.id.item_logout -> {
                showExitDialog()
                true
            }
            R.id.item_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.item_maps -> {
//                startActivity(Intent(this, MyMaps::class.java))
                val intent = Intent(this, MyMaps::class.java)
                intent.putExtra(TOKEN, iToken)
                Log.d("HomeStoryActivity", "ini token gan = $iToken")
                startActivity(intent)
                true
            }
            else -> {
                true
            }
        }
    }

    private fun getData() {
        val adapter = StoryAdapter()
        binding.rcyStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        storyViewModel.getAllStories().observe(this){
            if(it != null){
                adapter.submitData(lifecycle, it)
//                Log.d("HomeStoryActivity", " Berhasil gan = $it")
            }
        }
//    private fun playAnimation() {
////        val tvWelcome = ObjectAnimator.ofFloat(binding.tvWelcomeHome, View.ALPHA, 1F).setDuration(DURATION2)
////        val imgLogout = ObjectAnimator.ofFloat(binding.,View.ALPHA,1F).setDuration(DURATION)
////        val imgSetting = ObjectAnimator.ofFloat(binding.imgSetting, View.ALPHA, 1F).setDuration(DURATION)
//
////        AnimatorSet().apply{
////            playSequentially(tvWelcome)
////            start()
////        }
//    }

//    private fun showRecyclerList(arraylist: ArrayList<ListStoryItem>) {
//        rcyStory.layoutManager = LinearLayoutManager(this)
//        val listUserAdapter = StoryAdapter(arraylist)
//        rcyStory.adapter = listUserAdapter
//    }


//        storyViewModel.story.observe(this) {
//            if(it != null){
//                adapter.submitData(lifecycle, it)
//                Log.d("HomeStoryActivity"," Berhasil gan = "+ it.toString())
//            }else{
//                Log.d("HomeStoryActivity"," Gagal gan ")
//            }
//
//        }
      }

//    private fun getAllStory(token: String){
//        showLoading(true)
//
//        val client = ApiConfig.getApiService().getAllStory(token)
//        client.enqueue(object : Callback<GetAllStoryResponse> {
//            override fun onResponse(call: Call<GetAllStoryResponse>, response: Response<GetAllStoryResponse>) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        when(responseBody.error){
//                            true ->{
//                                showLoading(false)
//                            }
//                            false->{
//                                showLoading(false)
//                                showRecyclerList(responseBody.listStory as ArrayList<ListStoryItem>)
//                            }
//                        }
//                    }
//                } else {
//                    Log.e(RegisterActivity.TAG, "onFailure : " + response.message())
//                }
//            }
//            override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
//                Log.e(RegisterActivity.TAG, "onFailure: ${t.message}")
//            }
//        })
//    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progresBar.visibility = View.VISIBLE
        } else {
            binding.progresBar.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        showExitDialog()
    }

    private fun showExitDialog(){
        val pref = UserPreferencesDS.getInstance(dataStore)
        val exitDialog = AlertDialog.Builder(this)
        exitDialog.setTitle(resources.getString(R.string.exit))
        exitDialog.setMessage(resources.getString(R.string.isExit))
        exitDialog.setPositiveButton(android.R.string.yes) { _, _ ->
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//            mUserPreference.clearUserSession()
//            userVM.clearData()
//            UserVMDS.saveCurrentToken("")
            lifecycleScope.launch{
                pref.clearSession()
            }
            startActivity(intent)
        }

        exitDialog.setNegativeButton(android.R.string.no) { _, _ ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }
        exitDialog.show()
    }
}