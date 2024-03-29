package id.ac.ukdw.ti.mysubmission2.data.repo.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: ArrayList<ItemsItem>
)

data class ItemsItem(

	@field:SerializedName("subscriptions_url")
	val subscriptionsUrl: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("gists_url")
	val gistsUrl: String,

	@field:SerializedName("repos_url")
	val reposUrl: String,

	@field:SerializedName("html_url")
	val htmlUrl: String,

	@field:SerializedName("following_url")
	val followingUrl: String,

	@field:SerializedName("starred_url")
	val starredUrl: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("followers_url")
	val followersUrl: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("organizations_url")
	val organizationsUrl: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("score")
	val score: Int,

	@field:SerializedName("received_events_url")
	val receivedEventsUrl: String,

	@field:SerializedName("events_url")
	val eventsUrl: String,

	@field:SerializedName("site_admin")
	val siteAdmin: Boolean,

	@field:SerializedName("gravatar_id")
	val gravatarId: String,

	@field:SerializedName("node_id")
	val nodeId: String
)
