package id.ac.ukdw.sub1_intermediate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import id.ac.ukdw.sub1_intermediate.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        playAnimation()

        val astronotImage = findViewById<ImageView>(R.id.imageView)

        binding.btnRegister.setOnClickListener {
            when {
                binding.edtName.text.toString() == "" -> {
                    binding.edtName.error = "Names cannot be empty"
                }
                binding.edtEmail.text.toString() == "" -> {
                    binding.edtEmail.error = "Email cannot be empty"
                }
                binding.edtPassword.text.toString() == "" -> {
                    binding.edtPassword.error = "Password cannot be empty"
                }
                binding.edtPassword.text.toString().length < 6  -> {
                    binding.edtPassword.error = "Password consists of at least 6 characters"
                }
                else -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra(
                        "loginCommand",
                        "Please login first with the account you created earlier"
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            this, astronotImage, "astronotImage"
                        )
                    startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

//            if(!TextUtils.isEmpty(edtName)){
//                if(edtEmail.trim().length >= 0){
//                    if(edtPassword.trim().length >= 0){
//                        if(edtPassword.trim().length >= 6){
//                            val intent = Intent(this, LoginActivity::class.java)
//                            intent.putExtra(
//                                "loginCommand",
//                                "Please login first with the account you created earlier"
//                            )
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            val optionsCompat: ActivityOptionsCompat =
//                                ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                    this, astronotImage, "astronotImage"
//                                )
//                            startActivity(intent, optionsCompat.toBundle())
//                        }else{
//                            binding.edtPassword.error = "Password consists of at least 6 characters"
//                        }
//                    }else{
//                        binding.edtPassword.error = "Password cannot be empty"
//                    }
//                }else{
//                    binding.edtEmail.error = "Email cannot be empty"
//                }
//            }else{
//                binding.edtName.error = "Names cannot be empty"
//            }
//        }





//    private fun postUser() {
//
//        if()
////        showLoading(true)
//        val client = ApiConfig.getApiService().getUsers(keywordUser)
//        client.enqueue(object : Callback<UserResponse> {
//            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
//                showLoading(false)
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        showRecyclerList(responseBody.items)
//                    }else{
//                        Toast.makeText(this@MainActivity, resources.getString(R.string.userNotFound), Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Log.e(TAG, "onFailure: ${response.message()}")
//                    Toast.makeText(this@MainActivity, resources.getString(R.string.errorResponse), Toast.LENGTH_SHORT).show()
//                }
//            }
//            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
//                showLoading(false)
//                Log.e(TAG, "onFailure: ${t.message}")
//                Toast.makeText(this@MainActivity, resources.getString(R.string.errorResponse), Toast.LENGTH_SHORT).show()
//            }
//        })
//    }


    private fun playAnimation(){
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1F).setDuration(800)
        val tvRegister = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1F).setDuration(450)
        val tvName = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1F).setDuration(450)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1F).setDuration(450)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1F).setDuration(450)
        val edtName = ObjectAnimator.ofFloat(binding.edtName, View.ALPHA, 1F).setDuration(450)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1F).setDuration(450)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1F).setDuration(450)

        val textBox = AnimatorSet().apply{
            playTogether(tvName, edtName, tvEmail, edtEmail, tvPassword, edtPassword)
        }

        AnimatorSet().apply {
            playSequentially(tvRegister, textBox, btnRegister)
            start()
        }
    }

}