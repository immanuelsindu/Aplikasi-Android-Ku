package id.ac.ukdw.sub1_intermediate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import id.ac.ukdw.sub1_intermediate.databinding.ActivityLoginBinding

class LoginActivity:  AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        playAnimation()

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, HomeStoryActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        val userCommand = intent.getStringExtra("loginCommand")
        if(userCommand != ""){
            binding.tvUserCommand.text = userCommand
        }

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

}