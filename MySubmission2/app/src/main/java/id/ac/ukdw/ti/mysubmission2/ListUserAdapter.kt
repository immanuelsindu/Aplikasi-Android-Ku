package id.ac.ukdw.ti.mysubmission2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.ukdw.ti.mysubmission2.databinding.ActivityMainBinding
import id.ac.ukdw.ti.mysubmission2.databinding.ItemUserBinding

class ListUserAdapter(private val listUser: ArrayList<ItemsItem>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var binding : ActivityMainBinding

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


//    interface OnItemClickCallback {
//        fun onItemClicked(data : ItemsItem)
//    }
//
//    private lateinit var onItemClickCallback: OnItemClickCallback
//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }



    class ListViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        val (photo, username) = listUser[position]
        val photo = listUser[position].avatarUrl
        val username = listUser[position].login
        Glide.with(holder.itemView.context)
            .load(photo) // URL Gambar
            .circleCrop() // Mengubah image menjadi lingkaran
            .into(holder.binding.imgItemPhoto) // imageView mana yang akan diterapkan
        holder.binding.tvUsername.text = username

        holder.itemView.setOnClickListener{
            val sendData = Intent(holder.itemView.context, DetailActivity::class.java)
            sendData.putExtra("login",username)
            holder.itemView.context.startActivity(sendData)
        }
    }



    override fun getItemCount(): Int = listUser.size



}