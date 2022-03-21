package id.ac.ukdw.ti.mysubmission2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.ac.ukdw.ti.mysubmission2.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding


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
        var login = intent.getStringExtra("userLogin")
        binding.tvName.text = login

        val sectionsPagerAdapter = SectionsPagerAdapter(this, login.toString())
//        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        binding.viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        if (login != null) {
            findUser(login)
        }




    }

//    //BUG
//    private fun showRecyclerList(arraylist: ArrayList<FollowerResponseItem>) {
//        rvFollower.layoutManager = LinearLayoutManager(this)
//        val FollowerAdapter = FollowerAdapter(arraylist)
//        rvFollower.adapter = FollowerAdapter
//    }

    private fun findUser(login : String) {
        showLoading(true)
        val client = ApiConfig2.getApiService().getUser2(login)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>,response: Response<DetailResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        binding.tvName.text = responseBody.name
                        binding.tvLoginName.text = responseBody.login
                        binding.tcFollower.text = "Follower : "+ responseBody.followers
                        binding.tvFollowing.text = "Following :"+ responseBody.following
                        binding.tvCompany.text = responseBody.company
                        binding.tvLocation.text = responseBody.location
                        Glide.with(this@DetailActivity)
                            .load(responseBody.avatarUrl) // URL Gambar
                            .circleCrop() // Mengubah image menjadi lingkaran
                            .into(binding.circleImageView) // imageView mana yang akan diterapkan
                        //BUG
//                        showFollower(responseBody.login)
                    }
                } else {
                    Log.e(DetailActivity.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                showLoading(false)
                Log.e(DetailActivity.TAG, "onFailure: ${t.message}")
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

    //BUG
//    private fun showFollower(username : String){
//        val client2 = ApiConfigFollower.getApiService().getFollower(username)
//        client2.enqueue(object : Callback<FollowerResponse>{
//            override fun onResponse(call: Call<FollowerResponse>,response: Response<FollowerResponse>) {
//                if (response.isSuccessful){
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        showRecyclerList(responseBody.followerResponse)
//                    }
//                }
//            }
//            override fun onFailure(call: Call<FollowerResponse>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })
//    }
}




