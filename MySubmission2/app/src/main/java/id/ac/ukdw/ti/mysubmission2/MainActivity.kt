package id.ac.ukdw.ti.mysubmission2

import android.app.SearchManager
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.ukdw.ti.mysubmission2.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUsers: RecyclerView
    private  var keywordUser = ""
    private val list = ArrayList<User>()
    var insideList : ArrayList<User> = ArrayList<User>()

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        rvUsers = findViewById(R.id.rvUsers)
        rvUsers.setHasFixedSize(true)
        showLoading(false)
        val addAll = list.addAll(insideList)
        showRecyclerList()
    }
    private fun showRecyclerList() {
//        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            rvUsers.layoutManager = GridLayoutManager(this, 2)
//        } else {
//            rvUsers.layoutManager = LinearLayoutManager(this)
//        }

        val listUserAdapter = ListUserAdapter(list)
        rvUsers.adapter = listUserAdapter



//        listHeroAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: Hero) {
//                showSelectedHero(data)
//            }
//        })

    }

    private fun findUser() {
        showLoading(true)
        val client = ApiConfig.getApiService().getUsers(keywordUser)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>,response: Response<UserResponse>) {
                showLoading(false)
                val listUser = ArrayList<User>()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Toast.makeText(this@MainActivity, "Response berhasil", Toast.LENGTH_SHORT).show()
                        for(i in responseBody.items){
                            var newUser = User(i.avatarUrl,i.login)
                            listUser.add(newUser)
                        }
                    insideList = listUser
                        Toast.makeText(this@MainActivity, "Jumlah list = " +insideList.size, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Toast.makeText(this@MainActivity, "Response Tidak berhasil", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
                Toast.makeText(this@MainActivity, "Response Tidak Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
//                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                keywordUser = query
                findUser()
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }
}