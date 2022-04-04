package id.ac.ukdw.ti.mysubmission2.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import id.ac.ukdw.ti.mysubmission2.Injection
import id.ac.ukdw.ti.mysubmission2.R
import id.ac.ukdw.ti.mysubmission2.adapter.SectionsPagerAdapter
import id.ac.ukdw.ti.mysubmission2.data.local.entity.UsersEntity
import id.ac.ukdw.ti.mysubmission2.data.repo.api.ApiConfig
import id.ac.ukdw.ti.mysubmission2.data.repo.response.DetailResponse
import id.ac.ukdw.ti.mysubmission2.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity(){
    private var isFav: Boolean? = null
    private lateinit var binding: ActivityDetailBinding
    private lateinit var userRes: DetailResponse

    companion object {
        private const val TAG = "DetailActivity"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.detail_name)
        var login = intent.getStringExtra("userLogin")
        binding.tvName.text = login
        val sectionsPagerAdapter = SectionsPagerAdapter(this, login.toString())
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f



        val repo = Injection.provideRepository(this)
        if (login != null) {
            findUser(login)
            repo.isFavorite(login).observe(this){
                isFav = it
                if(it){
                    binding.fabFav.setImageDrawable(
                        ContextCompat.getDrawable(this,
                        R.drawable.ic_baseline_favorite_24
                    ))
                }else{
                    binding.fabFav.setImageDrawable(
                        ContextCompat.getDrawable(this,
                            R.drawable.ic_baseline_favorite_border_24
                        )
                    )
                }
            }
        }

        binding.fabFav.setOnClickListener{
            val usersEntity = UsersEntity(userRes.login, userRes.avatarUrl)
            if(isFav == true){
                repo.deleteUsers(usersEntity)
            }else{
                repo.insertUsers(usersEntity)
            }
        }
    }

    private fun findUser(login : String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getUser2(login)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        binding.tvName.text = responseBody.name
                        binding.tvLoginName.text = responseBody.login
                        binding.tcFollower.text =  getString(R.string.follower) + responseBody.followers
                        binding.tvFollowing.text = getString(R.string.following) +  responseBody.following
                        binding.tvCompany.text = responseBody.company
                        binding.tvLocation.text = responseBody.location
                        binding.tvRepo.text = getString(R.string.publicRepo) + responseBody.publicRepos.toString()
                        Glide.with(this@DetailActivity)
                            .load(responseBody.avatarUrl)
                            .circleCrop()
                            .into(binding.circleImageView)

                        userRes = responseBody
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }


}