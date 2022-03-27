package id.ac.ukdw.ti.mysubmission2.ui.favourite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.ukdw.ti.mysubmission2.Injection
import id.ac.ukdw.ti.mysubmission2.data.local.entity.UsersEntity
import id.ac.ukdw.ti.mysubmission2.databinding.ItemUserFavBinding
import id.ac.ukdw.ti.mysubmission2.ui.detail.DetailActivity

class FavouriteAdapter : RecyclerView.Adapter<FavouriteAdapter.FavoriteGithubUserHolder>() {
    private val listFavGithubUser = ArrayList<UsersEntity>()

    fun setListFavGithubUser(listFavGithubUser: List<UsersEntity>) {
        val diffCallback = UserCallback(this.listFavGithubUser,
            listFavGithubUser as ArrayList<UsersEntity>
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavGithubUser.clear()
        this.listFavGithubUser.addAll(listFavGithubUser)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteGithubUserHolder {
        val binding = ItemUserFavBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteGithubUserHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteGithubUserHolder, position: Int) {
        holder.bind(listFavGithubUser[position])
    }

     inner class FavoriteGithubUserHolder(private val binding: ItemUserFavBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(users: UsersEntity) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(users.avatarUrl)
                    .circleCrop()
                    .into(binding.imgItemPhoto)
                binding.tvUsername.text = users.login

                itemView.setOnClickListener{
                    val sendData = Intent(itemView.context, DetailActivity::class.java)
                    sendData.putExtra("userLogin",users.login)
                    itemView.context.startActivity(sendData)
                }

                imgFavDelete.setOnClickListener {
                    val repo = Injection.provideRepository(itemView.context)
                    repo.deleteUsers(users)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listFavGithubUser.size
    }
}