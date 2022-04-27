package com.example.storyapp.ui.stories.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.ui.stories.ListStoriesAdapter

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<ListStoryItem>(ListStoriesAdapter.GET_STORY) as ListStoryItem

        supportPostponeEnterTransition()
        Glide.with(applicationContext)
            .load(story.photoUrl)
            .listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    supportStartPostponedEnterTransition()
                    return false
                }

            })
            .into(binding.ivDetail)

        binding.tvAuthorNameStory.text = story.name
        binding.tvDescriptionStory.text = story.description
    }
}