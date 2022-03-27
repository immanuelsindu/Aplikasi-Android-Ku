package id.ac.ukdw.ti.mysubmission2.ui.detail.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.ukdw.ti.mysubmission2.adapter.FollowAdapter
import id.ac.ukdw.ti.mysubmission2.adapter.SectionsPagerAdapter
import id.ac.ukdw.ti.mysubmission2.data.repo.api.ApiConfig
import id.ac.ukdw.ti.mysubmission2.data.repo.response.FollowerResponseItem
import id.ac.ukdw.ti.mysubmission2.databinding.FragmentFollowerBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerFragment : Fragment() {

    private lateinit var binding : FragmentFollowerBinding
    companion object{
        private const val TAG = "FollowerFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userLogin = arguments?.getString(SectionsPagerAdapter.USER).toString()
        showFollower(userLogin)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showFollower(username : String){
        showLoading(true)
        val client2 = ApiConfig.getApiService().getFollower(username)
        client2.enqueue(object : Callback<ArrayList<FollowerResponseItem>> {
            override fun onResponse(call: Call<ArrayList<FollowerResponseItem>>, response: Response<ArrayList<FollowerResponseItem>>) {
                if (response.isSuccessful){
                    showLoading(false)
                    val responseBody = response.body()
                    if (responseBody != null) {
                        showRecyclerList(responseBody)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<FollowerResponseItem>>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun showRecyclerList(arraylist: ArrayList<FollowerResponseItem>) {
        binding.rvFollower.layoutManager = LinearLayoutManager(requireActivity())
        val FollowerAdapter = FollowAdapter(arraylist)
        binding.rvFollower.adapter = FollowerAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }
}