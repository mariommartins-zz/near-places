package com.mariomartins.nearplaces.data.source.remote.googleplaces.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaceResponse(
    val geometry: PlaceResponseGeometry? = null,
    val name: String?,
    val icon: String? = null,
    @Json(name = "place_id") val placeId: String?,
    @Json(name = "opening_hours") val openingHours: PlaceResponseOpenStatus? = null,
    val rating: Double? = null,
    val types: List<String>? = null
)

@JsonClass(generateAdapter = true)
data class PlaceResponseOpenStatus(@Json(name = "open_now") val openNow: Boolean? = null)

@JsonClass(generateAdapter = true)
data class PlaceResponseGeometry(val location: PlaceResponseLocation? = null)

@JsonClass(generateAdapter = true)
data class PlaceResponseLocation(val lat: Double? = null, val lng: Double? = null)
