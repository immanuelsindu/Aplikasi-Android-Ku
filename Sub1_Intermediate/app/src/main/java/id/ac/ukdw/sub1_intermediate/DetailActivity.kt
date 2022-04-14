package id.ac.ukdw.sub1_intermediate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import id.ac.ukdw.sub1_intermediate.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailModel = intent.getParcelableExtra<DetailStoryModel>("userModel") as DetailStoryModel

        binding.tvUserNameDetail.text = detailModel.name
        binding.tvDescDetail.text= detailModel.desc
        Glide.with(this)
            .load(detailModel.photo)
            .into(binding.imgDetail)



    }
}