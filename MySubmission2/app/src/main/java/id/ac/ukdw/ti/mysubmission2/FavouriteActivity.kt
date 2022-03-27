package id.ac.ukdw.ti.mysubmission2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FavouriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        supportActionBar?.title = resources.getString(R.string.favourite)
    }
}