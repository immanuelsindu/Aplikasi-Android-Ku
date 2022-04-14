package id.ac.ukdw.sub1_intermediate.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailStoryModel (
    var name: String= "",
    var desc: String= "",
    var photo: String= ""
) : Parcelable