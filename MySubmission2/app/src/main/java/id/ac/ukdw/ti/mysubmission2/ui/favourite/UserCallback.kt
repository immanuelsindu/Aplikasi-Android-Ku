package id.ac.ukdw.ti.mysubmission2.ui.favourite

import androidx.recyclerview.widget.DiffUtil
import id.ac.ukdw.ti.mysubmission2.data.local.entity.UsersEntity

class UserCallback(private val mOldGithubUserList: ArrayList<UsersEntity>, private val mNewGithubUserList: ArrayList<UsersEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldGithubUserList.size
    }

    override fun getNewListSize(): Int {
        return  mNewGithubUserList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldGithubUserList[oldItemPosition].login == mNewGithubUserList[newItemPosition].login
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }
}
