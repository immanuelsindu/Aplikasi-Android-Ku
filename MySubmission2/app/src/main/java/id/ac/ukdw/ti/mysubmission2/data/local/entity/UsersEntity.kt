package id.ac.ukdw.ti.mysubmission2.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
class UsersEntity(
    @field:ColumnInfo(name = "login")
    @field:PrimaryKey
    var login: String,

    @field:ColumnInfo(name = "avatar_url")
    var avatarUrl: String,

//    @field:ColumnInfo(name = "login")
//    val login: String,

//    @field:ColumnInfo(name = "bookmarked")
//    var isBookmarked: Boolean
): Parcelable