package id.ac.ukdw.ti.mysubmission2.ui.favourite


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.ukdw.ti.mysubmission2.Injection
import id.ac.ukdw.ti.mysubmission2.R
import id.ac.ukdw.ti.mysubmission2.adapter.ListUserAdapter
import id.ac.ukdw.ti.mysubmission2.data.local.entity.UsersEntity
import id.ac.ukdw.ti.mysubmission2.databinding.ActivityFavouriteBinding

class FavouriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouriteBinding
    private lateinit var adapter: FavouriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rcyFav.layoutManager = LinearLayoutManager(this)

        val repo = Injection.provideRepository(this)
        repo.getFavUser().observe(this) {
            adapter.setListFavGithubUser(it)
        }
        supportActionBar?.title = resources.getString(R.string.favourite)
        adapter = FavouriteAdapter()
        binding.rcyFav.setHasFixedSize(true)
        binding.rcyFav.adapter = adapter
    }


}