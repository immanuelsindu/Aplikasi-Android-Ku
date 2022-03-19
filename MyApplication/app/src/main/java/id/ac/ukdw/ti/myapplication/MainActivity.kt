package id.ac.ukdw.ti.myapplication

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.ukdw.ti.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUser: RecyclerView
    private val list = ArrayList<User>()

    companion object {
        const val EXTRA_USER = "extraUser"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUser.setHasFixedSize(true)

        val addAll = list.addAll(listUser)
        showRecylerList()
    }
    private val listUser: ArrayList<User>
        get() {
            val dataNama = resources.getStringArray(R.array.dataNama)
            val dataUsername = resources.getStringArray(R.array.dataUsername)
            val dataAvatar = resources.obtainTypedArray(R.array.dataAvatar)
            val dataFollowing = resources.getStringArray(R.array.dataFollowing)
            val dataFollower = resources.getStringArray(R.array.dataFollower)
            val dataCompany = resources.getStringArray(R.array.dataCompany)
            val dataLocation = resources.getStringArray(R.array.dataLocation)
            val dataRepository = resources.getStringArray(R.array.dataRepository)
            val listUser = ArrayList<User>()
            for (i in dataNama.indices) {
                val user = User(dataNama[i],dataUsername[i], dataAvatar.getResourceId(i, -1), dataFollowing[i],dataFollower[i],dataCompany[i],dataLocation[i], dataRepository[i])
                listUser.add(user)
            }
            return listUser
        }


    private fun showRecylerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(this)
        }
        binding.rvUser.setHasFixedSize(true)
        val listUserAdapter = ListUserAdapter(list)
        binding.rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                pindahKeDetailuser(data)
            }
        })
    }
    private fun pindahKeDetailuser(user: User) {
        val intent = Intent(this@MainActivity, DetailUser::class.java)
        intent.putExtra(EXTRA_USER, user)
        startActivity(intent)
    }
}