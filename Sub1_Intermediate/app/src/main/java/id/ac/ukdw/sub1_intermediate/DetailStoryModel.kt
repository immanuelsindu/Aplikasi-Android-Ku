package id.ac.ukdw.sub1_intermediate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class DetailStoryModel (
    var name: String= "",
    var desc: String= "",
    var photo: String= ""
) : Parcelable