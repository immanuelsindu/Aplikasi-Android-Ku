package id.ac.ukdw.ti.mysubmission2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UsersEntity(
    @field:ColumnInfo(name = "login")
    @field:PrimaryKey
    val login: String,

    @field:ColumnInfo(name = "avatar_url")
    val avatarUrl: String,

//    @field:ColumnInfo(name = "login")
//    val login: String,

    @field:ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean
)