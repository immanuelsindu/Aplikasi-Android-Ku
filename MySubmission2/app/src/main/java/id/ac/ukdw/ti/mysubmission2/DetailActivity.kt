package id.ac.ukdw.ti.mysubmission2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import id.ac.ukdw.ti.mysubmission2.databinding.ActivityDetailBinding
import id.ac.ukdw.ti.mysubmission2.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var login = intent.getStringExtra("login")
        binding.tvName.text = login

        if (login != null) {
            findUser(login)
        }

    }

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

//                        Toast.makeText(this@MainActivity, "Jumlah list = " + responseBody.items.size, Toast.LENGTH_SHORT).show()
                    }
                } else {
//                    Log.e(MainActivity.TAG, "onFailure: ${response.message()}")
//                    Toast.makeText(this@MainActivity, "Response Tidak berhasil", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                showLoading(false)
//                Log.e(MainActivity.TAG, "onFailure: ${t.message}")
//                Toast.makeText(this@MainActivity, "Response Tidak Failure", Toast.LENGTH_SHORT).show()
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