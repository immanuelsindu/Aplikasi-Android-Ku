package id.ac.ukdw.ti.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.ac.ukdw.ti.myapplication.MainActivity.Companion.EXTRA_USER
import id.ac.ukdw.ti.myapplication.databinding.DetailUserBinding

class DetailUser: AppCompatActivity(){
    private lateinit var binding: DetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        binding.circleImageView.setImageResource(user.avatar)
        binding.txtNama2.text = user.nama
        binding.txtUsername2.text = user.username
        binding.txtFollower.text = " Follower : " + user.follower
        binding.txtFollowing.text = "Following : " + user.following
        binding.txtCompany.text = user.company
        binding.txtLocation.text = user.location
        binding.txtRepository.text = "Repository : " + user.repository

    }

}