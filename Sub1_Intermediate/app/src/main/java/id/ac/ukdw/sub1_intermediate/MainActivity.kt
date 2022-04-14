package id.ac.ukdw.sub1_intermediate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import id.ac.ukdw.sub1_intermediate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var userModel: UserModel
    private lateinit var astronotImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        mUserPreference = UserPreference(this)

        userModel = mUserPreference.getUser()
        when{
            userModel.name.toString() != "" ->{
                val intent = Intent(this, HomeStoryActivity::class.java)
                intent.putExtra("name", userModel.name.toString())
                startActivity(intent)
            }
        }

        astronotImage = findViewById<ImageView>(R.id.imageView)
        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, astronotImage, "astronotImage"
                )
            startActivity(intent, optionsCompat.toBundle())
        }

        binding.buttonLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, astronotImage, "astronotImage")
            startActivity(intent, optionsCompat.toBundle())
        }

        playAnimation()
    }



    @SuppressLint("RestrictedApi")
    private fun playAnimation() {

        val logo = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1F).setDuration(450)
        val buttonLogin = ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1F).setDuration(450)
        val buttonRegister = ObjectAnimator.ofFloat(binding.buttonLogin, View.ALPHA, 1F).setDuration(450)
        val textWelcome = ObjectAnimator.ofFloat(binding.textViewWelcome, View.ALPHA, 1F).setDuration(450)

        val together = AnimatorSet().apply {
            playTogether(buttonLogin, buttonRegister)
        }

        AnimatorSet().apply{
            playSequentially(logo, textWelcome, together)
            start()
        }

    }
}