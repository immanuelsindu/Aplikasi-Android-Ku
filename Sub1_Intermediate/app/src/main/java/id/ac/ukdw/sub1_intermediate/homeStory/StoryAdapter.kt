package id.ac.ukdw.sub1_intermediate.homeStory

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import id.ac.ukdw.sub1_intermediate.databinding.ItemStoryBinding
import id.ac.ukdw.sub1_intermediate.detail.DetailActivity
import id.ac.ukdw.sub1_intermediate.detail.DetailStoryModel


class StoryAdapter(private val listStory: ArrayList<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    companion object{
        private const val VIEWMORE = "... view more"
        private const val USERMODEL = "userModel"
        private const val USERNAME = "username"
        private const val DESCRIPTION = "description"
        private const val IMAGEDETAIL = "imgDetail"
        private const val CONSTRAINTLAYOUT= "constraintLayout"
    }
    class ListViewHolder(var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val username = listStory[position].name
        var desc = listStory[position].description
        var desc2 = ""
        desc2 = when{
            desc.length > 40 -> {
                desc.slice(0..32) + VIEWMORE
            }else->{
                desc
            }
        }
        val image = listStory[position].photoUrl

        Glide.with(holder.itemView.context)
            .load(image)
            .listener(object: RequestListener<Drawable> {
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
                    holder.binding.progresBar.visibility = View.GONE
                    return false
                }
            })
            .into(holder.binding.imgStory)


        holder.binding.apply {
            tvUserName.text = username
            tvDesc.text = desc2
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            val detailModel = DetailStoryModel(username,desc, image)
            intent.putExtra(USERMODEL, detailModel)
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.tvUserName, USERNAME),
                    Pair(holder.binding.tvDesc, DESCRIPTION),
                    Pair(holder.binding.imgStory, IMAGEDETAIL),
                    Pair(holder.binding.contraintLayout, CONSTRAINTLAYOUT),
                )
            holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
        }
    }
    override fun getItemCount(): Int = listStory.size
}
