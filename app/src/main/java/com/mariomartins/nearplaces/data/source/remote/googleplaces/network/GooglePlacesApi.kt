package com.mariomartins.nearplaces.data.source.remote.googleplaces.network

import com.mariomartins.nearplaces.data.source.remote.googleplaces.response.PlaceResponse
import com.mariomartins.nearplaces.data.source.remote.googleplaces.response.ResponseWrapper
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

private const val DEFAULT_RADIUS = 1000

interface GooglePlacesApi {

    @GET("nearbysearch/json?")
    fun fetchPlacesAsync(
        @Query("location") location: String,
        @Query("radius") radius: Int = DEFAULT_RADIUS,
        @Query("type") type: String,
        @Query("pagetoken") pageToken: String? = null,
        @Query("key") key: String
    ): Deferred<ResponseWrapper<PlaceResponse>>
}
