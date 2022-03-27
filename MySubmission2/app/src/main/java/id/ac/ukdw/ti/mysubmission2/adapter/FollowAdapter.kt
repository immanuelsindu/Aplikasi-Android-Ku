package id.ac.ukdw.ti.mysubmission2.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.ukdw.ti.mysubmission2.ui.detail.DetailActivity
import id.ac.ukdw.ti.mysubmission2.data.repo.response.FollowerResponseItem
import id.ac.ukdw.ti.mysubmission2.databinding.ItemUserBinding

class FollowAdapter(private val listFollower: ArrayList<FollowerResponseItem>) : RecyclerView.Adapter<FollowAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val photo = listFollower[position].avatarUrl
        val username = listFollower[position].login
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
    }
    override fun getItemCount(): Int = listFollower.size
}