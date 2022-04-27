package com.example.storyapp.ui.stories

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.StoryItemBinding
import com.example.storyapp.ui.stories.detail.DetailStoryActivity

class ListStoriesAdapter:
    PagingDataAdapter<ListStoryItem, ListStoriesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: StoryItemBinding): RecyclerView.ViewHolder(binding
        .root) {
        fun bind(story: ListStoryItem) {
            binding.tvAuthorNameStory.text = story.name
            binding.tvDescriptionStory.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .placeholder(R.drawable.ic_place_holder)
                .into(binding.ivStory)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(GET_STORY, story)

                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.ivStory, "profile"),
                    Pair(binding.tvAuthorNameStory, "name"),
                    Pair(binding.tvDescriptionStory, "description")
                )

                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataStoryItem = getItem(position)
        if (dataStoryItem != null) {
            holder.bind(dataStoryItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return MyViewHolder(binding)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }

        const val GET_STORY = "STORY"
    }
}