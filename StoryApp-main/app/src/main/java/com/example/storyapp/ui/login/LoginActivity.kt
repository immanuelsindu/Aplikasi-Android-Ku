package com.example.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.data.Result
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.di.Injection
import com.example.storyapp.ui.registration.RegistrationActivity
import com.example.storyapp.ui.stories.ListStoriesActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val storyRepository = Injection.provideRepository(this)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(storyRepository))[LoginViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPass.text.toString()
            viewModel.login(email, password).observe(this) {
                when (it) {
                    is Result.Success -> {
                        binding.loginLoading.visibility = View.GONE
                        login(it.data)
                    }
                    is Result.Loading -> {
                        binding.loginLoading.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding.loginLoading.visibility = View.GONE
                        Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            moveIntent(RegistrationActivity::class.java)
        }
    }

    private fun moveIntent(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    private fun login(isLogin: Boolean) {
        if (isLogin) {
            moveIntent(ListStoriesActivity::class.java)
            finish()
        }

        val message = if (isLogin) {
            getString(R.string.login_success)
        } else {
            getString(R.string.login_failed)
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}