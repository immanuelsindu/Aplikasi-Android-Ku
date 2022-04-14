package id.ac.ukdw.sub1_intermediate.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import id.ac.ukdw.sub1_intermediate.R
import id.ac.ukdw.sub1_intermediate.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    companion object{
        private const val USERMODEL = "userModel"
    }
    private lateinit var binding : ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailModel = intent.getParcelableExtra<DetailStoryModel>(USERMODEL) as DetailStoryModel
        supportActionBar?.title = resources.getString(R.string.titleDetailStory)
        binding.tvUserNameDetail.text = detailModel.name
        binding.tvDescDetail.text= detailModel.desc
        Glide.with(this)
            .load(detailModel.photo)
            .into(binding.imgDetail)
    }
}