package id.ac.ukdw.sub1_intermediate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import id.ac.ukdw.sub1_intermediate.databinding.ActivityHomeStoryBinding



class HomeStoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeStoryBinding
    private lateinit var mUserPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Story"
        playAnimation()

        mUserPreference = UserPreference(this)
        val name = intent.getStringExtra("name")
        binding.tvWelcomeHome.text = "Welcome Home, $name"

        binding.fabAddStory .setOnClickListener {
            val intent = Intent(this, NewStoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        binding.imgLogout.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            mUserPreference.clearUserSession()
            startActivity(intent)
        }
    }

    private fun playAnimation(){
        val tvWelcome = ObjectAnimator.ofFloat(binding.tvWelcomeHome, View.ALPHA, 1F).setDuration(1250)
        val imgLogout = ObjectAnimator.ofFloat(binding.imgLogout,View.ALPHA,1F).setDuration(450)

        AnimatorSet().apply{
            playSequentially(tvWelcome, imgLogout)
            start()
        }
    }


}