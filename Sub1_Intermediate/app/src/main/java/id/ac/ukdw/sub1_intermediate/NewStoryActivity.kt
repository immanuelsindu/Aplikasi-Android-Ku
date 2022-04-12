package id.ac.ukdw.sub1_intermediate

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.ac.ukdw.sub1_intermediate.databinding.ActivityNewStoryBinding

class NewStoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "New Story"

        binding.btnUpload.setOnClickListener {
            val intent = Intent(this, HomeStoryActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }
}