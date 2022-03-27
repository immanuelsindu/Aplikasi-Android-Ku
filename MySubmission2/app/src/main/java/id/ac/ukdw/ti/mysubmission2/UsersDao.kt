package id.ac.ukdw.ti.mysubmission2

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UsersDao{
    @Query("SELECT * FROM users ")
    fun getUsers(): LiveData<List<UsersEntity>>

    @Query("SELECT * FROM users where bookmarked = 1")
    fun getBookmarkedNews(): LiveData<List<UsersEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUsers(news: List<UsersEntity>)

//    @Update
//    fun updateNews(news: UsersEntity)

    @Query("DELETE FROM users WHERE bookmarked = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM users WHERE login = :login AND bookmarked = 1)")
    fun isNewsBookmarked(login: String): Boolean
}