package id.ac.ukdw.sub1_intermediate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import id.ac.ukdw.sub1_intermediate.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity:  AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        playAnimation()
        showLoading(false)
        val userCommand = intent.getStringExtra("loginCommand")
        if(userCommand != ""){
            binding.tvUserCommand.text = userCommand
        }

        binding.btnLogin.setOnClickListener {
           when{
               binding.edtEmail.text.toString() == "" ->{
                    binding.edtEmail.error = "Email cannot be empty"
               }
               binding.edtPassword.text.toString() == "" ->{
                   binding.edtPassword.error = "Password cannot be empty"
               }
               else ->{
                   postLogin(binding.edtEmail.text.toString(),binding.edtPassword.text.toString())
               }
           }
        }
    }

    private fun intentToHomeStory(name: String){

        val intent = Intent(this, HomeStoryActivity::class.java)
        intent.putExtra("name",name)
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
    }

    private fun postLogin(email: String, password: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().loginUser(email,password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        when(responseBody.error){
                            true ->{
                                showLoading(false)
                                Toast.makeText(this@LoginActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                            }
                            false->{
                                showLoading(false)

                                Toast.makeText(this@LoginActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                                intentToHomeStory(responseBody.loginResult.name)
                            }
                        }
                    }
                } else {
                    Log.e(RegisterActivity.TAG, "onFailure : " + response.message())
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(RegisterActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }


    private fun playAnimation(){
        val tvLogin= ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1F).setDuration(450)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1F).setDuration(450)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1F).setDuration(450)
        val tvPassword= ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1F).setDuration(450)
        val edtPassword= ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1F).setDuration(450)
        val btnLogin= ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1F).setDuration(450)

        val textbox = AnimatorSet().apply(){
            playTogether(tvEmail, edtEmail, tvPassword, edtEmail, edtPassword)
        }

        AnimatorSet().apply(){
            playSequentially(tvLogin, textbox, btnLogin)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progresBar.visibility = View.VISIBLE
        } else {
            binding.progresBar.visibility = View.GONE
        }
    }

}