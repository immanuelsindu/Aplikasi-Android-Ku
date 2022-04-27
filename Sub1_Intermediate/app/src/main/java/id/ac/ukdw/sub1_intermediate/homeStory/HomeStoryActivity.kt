
package id.ac.ukdw.sub1_intermediate.homeStory

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import id.ac.ukdw.sub1_intermediate.*
import id.ac.ukdw.sub1_intermediate.UserPreference
import id.ac.ukdw.sub1_intermediate.databinding.ActivityHomeStoryBinding
import id.ac.ukdw.sub1_intermediate.main.MainActivity
import id.ac.ukdw.sub1_intermediate.newStory.NewStoryActivity


class HomeStoryActivity : AppCompatActivity() {
    companion object{
        private const val BEARER = "Bearer "
        private const val DURATION = 450.toLong()
        private const val DURATION2 = 1250.toLong()
    }
    private lateinit var binding : ActivityHomeStoryBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var rcyStory: RecyclerView
    private lateinit var userVM: UserViewModel
//    private lateinit var storyViewModel: StoryViewModel

    private val storyViewModel: StoryViewModel by viewModels {
        ViewModelFactory_Paging(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        supportActionBar?.title = resources.getString(R.string.titleStoryActivity)
        playAnimation()
        showLoading(false)

        mUserPreference = UserPreference(this)
        rcyStory = findViewById(R.id.rcyStory)




        val name = mUserPreference.getUserName()
//        userVM = ViewModelProvider(this)[UserViewModel::class.java]
//        val userModel = userVM.getUser()
        if(name != "" && name != null){
            supportActionBar?.title = resources.getString(R.string.welcome_home,name)
            val token = BEARER + mUserPreference.getToken()
//            getAllStory(token)
            getData()
        }else{
            supportActionBar?.title = resources.getString(R.string.welcomeGuest)
        }

        binding.fabAddStory .setOnClickListener {
            val intent = Intent(this, NewStoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
//
//        binding.imgLogout.setOnClickListener{
//            showExitDialog()
//        }
//        binding.imgSetting.setOnClickListener{
//            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
                startActivity(Intent(this, MyMaps::class.java))
                true
            }
            else -> {
                true
            }
        }
    }
    private fun playAnimation(){
//        val tvWelcome = ObjectAnimator.ofFloat(binding.tvWelcomeHome, View.ALPHA, 1F).setDuration(DURATION2)
//        val imgLogout = ObjectAnimator.ofFloat(binding.,View.ALPHA,1F).setDuration(DURATION)
//        val imgSetting = ObjectAnimator.ofFloat(binding.imgSetting, View.ALPHA, 1F).setDuration(DURATION)

//        AnimatorSet().apply{
//            playSequentially(tvWelcome)
//            start()
//        }
    }

//    private fun showRecyclerList(arraylist: ArrayList<ListStoryItem>) {
//        rcyStory.layoutManager = LinearLayoutManager(this)
//        val listUserAdapter = StoryAdapter(arraylist)
//        rcyStory.adapter = listUserAdapter
//    }

    private fun getData() {
        val adapter = StoryAdapter()
        binding.rcyStory.adapter = adapter
        storyViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
//
//        val adapter = QuoteListAdapter()
//        binding.rvQuote.adapter = adapter
//        mainViewModel.quote.observe(this, {
//            adapter.submitData(lifecycle, it)
//        })
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
//                                showRecyclerList(responseBody.listStory)
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
        val exitDialog = AlertDialog.Builder(this)
        exitDialog.setTitle(resources.getString(R.string.exit))
        exitDialog.setMessage(resources.getString(R.string.isExit))
        exitDialog.setPositiveButton(android.R.string.yes) { _, _ ->
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            mUserPreference.clearUserSession()
//            userVM.clearData()

            startActivity(intent)
        }

        exitDialog.setNegativeButton(android.R.string.no) { _, _ ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }
        exitDialog.show()
    }



}