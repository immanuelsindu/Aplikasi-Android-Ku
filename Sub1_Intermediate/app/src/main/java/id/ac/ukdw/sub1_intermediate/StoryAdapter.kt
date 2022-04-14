package id.ac.ukdw.sub1_intermediate

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.ukdw.sub1_intermediate.databinding.ItemStoryBinding


class StoryAdapter(private val listStory: ArrayList<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val username = listStory[position].name
        var desc = listStory[position].description
        when{
            desc.length > 40 -> {
                desc = desc.slice(0..32) + "...view more"
            }
        }
        val image = listStory[position].photoUrl

        Glide.with(holder.itemView.context)
            .load(image)
            .into(holder.binding.imgStory)


        holder.binding.apply {
            tvUserName.text = username
            tvDesc.text = desc
        }

        if(holder.binding.imgStory.drawable != null){
            holder.binding.progresBar.visibility = View.GONE
        }


        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            val detailModel = DetailStoryModel(username,desc, image)
            intent.putExtra("userModel", detailModel)
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.tvUserName, "username"),
                    Pair(holder.binding.tvDesc, "description"),
                    Pair(holder.binding.imgStory, "imgDetail"),
                    Pair(holder.binding.contraintLayout, "constraintLayout"),

                )
            holder.itemView.context.startActivity(intent, optionsCompat.toBundle())

        }
    }
    override fun getItemCount(): Int = listStory.size
}
        //
//            val photo = listStory[position].avatarUrl
//            val username = listStory[position].login
//            Glide.with(holder.itemView.context)
//                .load(photo)
//                .circleCrop()
//                .into(holder.binding.imgItemPhoto)
//            holder.binding.tvUsername.text = username
//
//            holder.itemView.setOnClickListener{
//                val sendData = Intent(holder.itemView.context, DetailActivity::class.java)
//                sendData.putExtra("userLogin",username)
//                holder.itemView.context.startActivity(sendData)
//            }