package id.ac.ukdw.sub1_intermediate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import id.ac.ukdw.sub1_intermediate.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        playAnimation()

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