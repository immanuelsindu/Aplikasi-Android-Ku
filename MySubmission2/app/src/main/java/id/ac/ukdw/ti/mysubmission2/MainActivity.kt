package id.ac.ukdw.ti.mysubmission2

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
//    private var list = ArrayList<ItemsItem>()
//    var insideList : ArrayList<User> = ArrayList<User>()

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
//        val addAll = list.addAll(insideList)
        findUser()
    }
    private fun showRecyclerList(arraylist: ArrayList<ItemsItem>) {
        rvUsers.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(arraylist)
        rvUsers.adapter = listUserAdapter
    }


    private fun findUser() {
        showLoading(true)
        val client = ApiConfig.getApiService().getUsers(keywordUser)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>,response: Response<UserResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        showRecyclerList(responseBody.items)
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