package com.riezki.vixnewsapp.model.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class NewsResponse(

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("articles")
	val articles: MutableList<ArticlesItem>? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
