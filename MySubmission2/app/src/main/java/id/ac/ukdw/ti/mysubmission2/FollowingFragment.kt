package id.ac.ukdw.ti.mysubmission2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.ukdw.ti.mysubmission2.databinding.FragmentFollowerBinding
import id.ac.ukdw.ti.mysubmission2.databinding.FragmentFollowingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {

    private lateinit var binding :FragmentFollowingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userLogin = arguments?.getString(SectionsPagerAdapter.USER).toString()
        showFollower(userLogin)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showFollower(username : String){
        val client2 = ApiConfigFollower.getApiService().getFollowing(username)
        client2.enqueue(object : Callback<ArrayList<FollowerResponseItem>> {
            override fun onResponse(call: Call<ArrayList<FollowerResponseItem>>, response: Response<ArrayList<FollowerResponseItem>>) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        showRecyclerList(responseBody)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<FollowerResponseItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun showRecyclerList(arraylist: ArrayList<FollowerResponseItem>) {
        binding.rcyFollowing.layoutManager = LinearLayoutManager(requireActivity())
        val FollowerAdapter = FollowAdapter(arraylist)
        binding.rcyFollowing.adapter = FollowerAdapter
    }
}