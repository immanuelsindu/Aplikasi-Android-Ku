package id.ac.ukdw.sub1_intermediate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.ac.ukdw.sub1_intermediate.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

    }
}