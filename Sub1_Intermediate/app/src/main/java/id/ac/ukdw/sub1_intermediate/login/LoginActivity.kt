package id.ac.ukdw.sub1_intermediate.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import id.ac.ukdw.sub1_intermediate.R
import id.ac.ukdw.sub1_intermediate.UserPreference
import id.ac.ukdw.sub1_intermediate.api.ApiConfig
import id.ac.ukdw.sub1_intermediate.databinding.ActivityLoginBinding
import id.ac.ukdw.sub1_intermediate.homeStory.HomeStoryActivity
import id.ac.ukdw.sub1_intermediate.homeStory.UserModel
import id.ac.ukdw.sub1_intermediate.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity:  AppCompatActivity() {
    companion object{
        internal lateinit var userPreference: UserPreference
        private const val LOGINCOMMAND = "loginCommand"
        private const val INVALIDPASSWORD = "Invalid password"
        private const val EMAILFALSE = "\"email\" must be a valid email"
        private const val USERNOTFOUND = "User not found"
        private const val BADREQUEST = "Bad Request"
        private const val UNAUTHORIZED = "Unauthorized"
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        playAnimation()
        showLoading(false)
        val userCommand = intent.getStringExtra(LOGINCOMMAND)
        if(userCommand != ""){
            binding.tvUserCommand.text = userCommand
        }
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

        binding.btnLogin.setOnClickListener {
           when{
               binding.edtEmail.text.toString() == "" ->{
                    binding.edtEmail.error = resources.getString(R.string.emailCannotEmpty)
               }
               binding.edtPassword.text.toString() == "" ->{
                   binding.edtPassword.error = resources.getString(R.string.passwordCannotEmpty)
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
                                when(responseBody.message){
                                    INVALIDPASSWORD ->{
                                        Toast.makeText(this@LoginActivity,getString(R.string.invalidpassword), Toast.LENGTH_SHORT).show()
                                    }
                                    USERNOTFOUND->{
                                        Toast.makeText(this@LoginActivity,getString(R.string.usernotfound), Toast.LENGTH_SHORT).show()
                                    }
                                   EMAILFALSE ->{
                                        Toast.makeText(this@LoginActivity,getString(R.string.emailmustbeavalidemail), Toast.LENGTH_SHORT).show()
                                   }
                                }
                            }
                            false->{
                                showLoading(false)
                                saveUserSession(responseBody.loginResult.name, responseBody.loginResult.userId, responseBody.loginResult.token)
                                Toast.makeText(this@LoginActivity,getString(R.string.loginSuccessfully), Toast.LENGTH_SHORT).show()
                                intentToHomeStory(responseBody.loginResult.name)
                            }
                        }
                    }
                } else {
                    showLoading(false)
                    when(response.message()){
                        BADREQUEST->{
                            Toast.makeText(this@LoginActivity,getString(R.string.makeSureAccountCorrect), Toast.LENGTH_SHORT).show()
                        }
                        UNAUTHORIZED->{
                            Toast.makeText(this@LoginActivity,getString(R.string.makeSureAccountCorrect), Toast.LENGTH_SHORT).show()
                        }
                    }
                    Log.e(RegisterActivity.TAG, "onFailure : " + response.message())
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(RegisterActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun saveUserSession(name: String, id: String, token: String){
        userPreference = UserPreference(this)
        var userModel = UserModel()
        userModel.name = name
        userModel.id = id
        userModel.token = token
        userPreference.setUser(userModel)
    }


    private fun playAnimation(){
        val tvLogin= ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1F).setDuration(450)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1F).setDuration(450)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1F).setDuration(450)
        val tvPassword= ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1F).setDuration(450)
        val edtPassword= ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1F).setDuration(450)
        val btnLogin= ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1F).setDuration(450)

        val textbox = AnimatorSet().apply {
            playTogether(tvEmail, edtEmail, tvPassword, edtEmail, edtPassword)
        }

        AnimatorSet().apply {
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