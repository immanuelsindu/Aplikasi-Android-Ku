package com.example.storyapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivitySplashBinding
import com.example.storyapp.di.Injection
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.ui.stories.ListStoriesActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val storyRepository = Injection.provideRepository(this)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(storyRepository))[SplashViewModel::class.java]

        showProgress()
    }

    private fun showProgress() {
        Handler(Looper.getMainLooper()).postDelayed({
            val progress = binding.loadingSplash.progress + 10
            binding.loadingSplash.progress = progress

            if (progress < 100) {
                showProgress()
            } else {
                val intent = Intent(
                    this, if (viewModel.getTokenSession().isNotEmpty())
                        ListStoriesActivity::class.java
                    else
                        LoginActivity::class.java
                )
                startActivity(intent)
                finish()
            }

        }, 300)
    }
}