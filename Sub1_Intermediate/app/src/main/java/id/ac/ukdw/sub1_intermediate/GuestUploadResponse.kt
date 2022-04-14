package id.ac.ukdw.sub1_intermediate

import com.google.gson.annotations.SerializedName

data class GuestUploadResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
