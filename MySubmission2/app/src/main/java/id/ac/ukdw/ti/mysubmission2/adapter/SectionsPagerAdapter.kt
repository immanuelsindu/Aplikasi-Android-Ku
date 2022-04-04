package id.ac.ukdw.ti.mysubmission2.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.ac.ukdw.ti.mysubmission2.ui.detail.fragment.FollowerFragment
import id.ac.ukdw.ti.mysubmission2.ui.detail.fragment.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private val userLogin: String) : FragmentStateAdapter(activity) {

    companion object {
        const val USER = "userLogin"
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        val bundle = Bundle()
        bundle.putString(USER, userLogin)

        when (position) {
            0 ->{
                fragment = FollowerFragment()
                fragment.arguments = bundle
            }
            1 ->{
                fragment =  FollowingFragment()
                fragment.arguments = bundle
            }
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }

}