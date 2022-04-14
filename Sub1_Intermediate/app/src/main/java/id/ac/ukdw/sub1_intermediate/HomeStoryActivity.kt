
package id.ac.ukdw.sub1_intermediate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.ukdw.sub1_intermediate.databinding.ActivityHomeStoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeStoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeStoryBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var rcyStory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Story"
        playAnimation()

        mUserPreference = UserPreference(this)
        val name = mUserPreference.getUserName()
        showLoading(false)
        getAllStory()

        rcyStory = findViewById<RecyclerView>(R.id.rcyStory)
        binding.tvWelcomeHome.text = "Welcome Home, $name"
        binding.fabAddStory .setOnClickListener {
            val intent = Intent(this, NewStoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        binding.imgLogout.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            mUserPreference.clearUserSession()
            startActivity(intent)
        }
    }

    private fun playAnimation(){
        val tvWelcome = ObjectAnimator.ofFloat(binding.tvWelcomeHome, View.ALPHA, 1F).setDuration(1250)
        val imgLogout = ObjectAnimator.ofFloat(binding.imgLogout,View.ALPHA,1F).setDuration(450)

        AnimatorSet().apply{
            playSequentially(tvWelcome, imgLogout)
            start()
        }
    }

    private fun showRecyclerList(arraylist: ArrayList<ListStoryItem>) {
        rcyStory.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = StoryAdapter(arraylist)
        rcyStory.adapter = listUserAdapter
    }

    private fun getAllStory(){
        showLoading(true)
        mUserPreference = UserPreference(this)
        val token = "Bearer "+mUserPreference.getToken()
        val client = ApiConfig.getApiService().getAllStory(token)
        client.enqueue(object : Callback<GetAllStoryResponse> {
            override fun onResponse(call: Call<GetAllStoryResponse>, response: Response<GetAllStoryResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        when(responseBody.error){
                            true ->{
                                showLoading(false)
                            }
                            false->{
                                showLoading(false)
                                showRecyclerList(responseBody.listStory)
//                                saveUserSession(responseBody.loginResult.name, responseBody.loginResult.userId, responseBody.loginResult.token)
////                                myToken = responseBody.loginResult.token
//                                intentToHomeStory(responseBody.loginResult.name)
                            }
                        }
                    }
                } else {
                    Log.e(RegisterActivity.TAG, "onFailure : " + response.message())
                }
            }
            override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                Log.e(RegisterActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progresBar.visibility = View.VISIBLE
        } else {
            binding.progresBar.visibility = View.GONE
        }
    }


}