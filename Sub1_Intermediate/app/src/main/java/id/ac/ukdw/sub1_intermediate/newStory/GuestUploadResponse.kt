package id.ac.ukdw.sub1_intermediate.newStory

import com.google.gson.annotations.SerializedName

data class GuestUploadResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
