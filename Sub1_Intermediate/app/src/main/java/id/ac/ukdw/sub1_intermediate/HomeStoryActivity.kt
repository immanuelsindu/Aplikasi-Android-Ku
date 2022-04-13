package id.ac.ukdw.sub1_intermediate

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.ac.ukdw.sub1_intermediate.databinding.ActivityHomeStoryBinding
import id.ac.ukdw.sub1_intermediate.databinding.ActivityHomeStoryBinding.inflate
import id.ac.ukdw.sub1_intermediate.databinding.ActivityMainBinding


class HomeStoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Story"

        val name = intent.getStringExtra("name")
        binding.tvWelcomeHome.text = "Welcome Home, $name"
        binding.fabAddStory .setOnClickListener {
            val intent = Intent(this, NewStoryActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }





    }
}