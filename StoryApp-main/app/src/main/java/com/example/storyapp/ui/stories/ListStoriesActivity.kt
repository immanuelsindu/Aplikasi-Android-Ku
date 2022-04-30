package com.example.storyapp.ui.stories

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityListStoriesBinding
import com.example.storyapp.di.Injection
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.ui.stories.add.AddStoryActivity
import com.example.storyapp.ui.stories.map.MapsStoryActivity

class ListStoriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListStoriesBinding
    private lateinit var viewModel: ListStoriesViewModel

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AddStoryActivity.RESULT_CODE && result.data != null) {
            val uploadStatus = result.data?.getBooleanExtra(AddStoryActivity.UPLOAD_STATUS, false)
            if (uploadStatus == true) {
                setListStories()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyRepository = Injection.provideRepository(this)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(storyRepository)
        )[ListStoriesViewModel::class.java]

        binding.rvStories.layoutManager = LinearLayoutManager(this)
        setListStories()

        binding.fabAddStory.setOnClickListener {
            resultLauncher.launch(Intent(this@ListStoriesActivity, AddStoryActivity::class.java))
        }

        binding.fabMapStory.setOnClickListener {
            startActivity(Intent(this@ListStoriesActivity, MapsStoryActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_logout -> {
            viewModel.clearSession()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            true
        }
        R.id.action_change_language -> {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action_bar, menu)
        return true
    }

    private fun setListStories() {
        val adapter = ListStoriesAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        viewModel.findStories().observe(this) {
            if (it != null) {
                adapter.submitData(lifecycle, it)
//                Log.d("ListStoriesActivity"," Berhasil gan = "+ it.toString())
            }
        }
    }
}