package id.ac.ukdw.sub1_intermediate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel (
    var name: String? = null,
    var id : String? = null,
    var token: String? = null,
): Parcelable