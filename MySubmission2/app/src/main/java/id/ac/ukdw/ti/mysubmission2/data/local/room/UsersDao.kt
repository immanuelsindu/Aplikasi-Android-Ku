package id.ac.ukdw.ti.mysubmission2.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import id.ac.ukdw.ti.mysubmission2.data.local.entity.UsersEntity

@Dao
interface UsersDao{
    @Query("SELECT * FROM users ")
    fun getUsers(): LiveData<List<UsersEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUsers(user: UsersEntity)

    @Delete
    fun deleteUsers(user: UsersEntity)

    @Query("SELECT EXISTS(SELECT * FROM users WHERE login = :login )")
    fun isFavourite(login: String): LiveData<Boolean>
}