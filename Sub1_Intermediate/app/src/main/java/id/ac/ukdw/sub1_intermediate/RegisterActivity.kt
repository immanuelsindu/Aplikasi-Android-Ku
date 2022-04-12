package id.ac.ukdw.sub1_intermediate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import id.ac.ukdw.sub1_intermediate.databinding.ActivityRegisterBinding


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
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("loginCommand", "Please login first with the account you created earlier")
            intent.flags =  Intent.FLAG_ACTIVITY_CLEAR_TASK
            val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, astronotImage, "astronotImage")
            startActivity(intent, optionsCompat.toBundle())
        }




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