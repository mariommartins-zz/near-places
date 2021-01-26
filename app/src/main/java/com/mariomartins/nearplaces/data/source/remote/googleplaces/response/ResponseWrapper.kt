package com.mariomartins.nearplaces.data.source.remote.googleplaces.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseWrapper<R>(
    @Json(name = "next_page_token") val nextPageToken: String? = null,
    val results: List<R>,
    val status: String
)
