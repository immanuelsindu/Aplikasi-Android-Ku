package id.ac.ukdw.ti.mysubmission2

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.ukdw.ti.mysubmission2.databinding.ItemUserBinding

class ListUserAdapter(private val listUser: ArrayList<ItemsItem>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val photo = listUser[position].avatarUrl
        val username = listUser[position].login
        Glide.with(holder.itemView.context)
            .load(photo)
            .circleCrop()
            .into(holder.binding.imgItemPhoto)
        holder.binding.tvUsername.text = username

        holder.itemView.setOnClickListener{
            val sendData = Intent(holder.itemView.context, DetailActivity::class.java)
            sendData.putExtra("userLogin",username)
            holder.itemView.context.startActivity(sendData)
        }

        val ivBookmark = holder.binding.ivBookmark
//        if (news.isBookmarked) {
//            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_baseline_favorite_24))
//        } else {
//            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_baseline_favorite_border_24))
//        }
//        ivBookmark.setOnClickListener {
//            onBookmarkClick(news)
//        }
    }
    override fun getItemCount(): Int = listUser.size
}