package id.ac.ukdw.ti.mysubmission2.ui.favourite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.ac.ukdw.ti.mysubmission2.R

class FavouriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        supportActionBar?.title = resources.getString(R.string.favourite)
    }
}