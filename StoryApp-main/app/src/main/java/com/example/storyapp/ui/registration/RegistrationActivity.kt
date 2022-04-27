package com.example.storyapp.ui.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.data.Result
import com.example.storyapp.databinding.ActivityRegistrationBinding
import com.example.storyapp.di.Injection
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.ui.login.LoginViewModel

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var viewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val storyRepository = Injection.provideRepository(this)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(storyRepository))[RegistrationViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val name = binding.edtName.text.toString()
            val pass = binding.edtPass.text.toString()

            viewModel.registration(name, email, pass).observe(this) {
                when(it) {
                    is Result.Success -> {
                        binding.loadingRegister.visibility = View.GONE
                        Toast.makeText(this, it.data, Toast.LENGTH_LONG).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is Result.Loading -> {
                        binding.loadingRegister.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding.loadingRegister.visibility = View.GONE
                        Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}