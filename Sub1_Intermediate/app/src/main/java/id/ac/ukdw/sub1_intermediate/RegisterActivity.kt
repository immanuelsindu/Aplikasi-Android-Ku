package id.ac.ukdw.sub1_intermediate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import id.ac.ukdw.sub1_intermediate.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {
    companion object{
        const val TAG = "RegisterActivity"
    }
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        playAnimation()
        showLoading(false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //do nothing
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //doing something in MyEditText
            }
            override fun afterTextChanged(s: Editable) {
                //do nothing
            }
        })

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
                else -> {
                    postRegister(binding.edtName.text.toString(),binding.edtEmail.text.toString(),binding.edtPassword.text.toString())
                }
            }
        }
    }

    private fun intentToLogin(){
        val astronotImage = findViewById<ImageView>(R.id.imageView)
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progresBar.visibility = View.VISIBLE
        } else {
            binding.progresBar.visibility = View.GONE
        }
    }

    private fun postRegister(name : String, email: String, password: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().registerUser(name,email,password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        when(responseBody.error){
                            true ->{
                                showLoading(false)
                                Toast.makeText(this@RegisterActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                            }
                            false->{
                                showLoading(false)
                                Toast.makeText(this@RegisterActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                                intentToLogin()
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure : " + response.message())
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


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