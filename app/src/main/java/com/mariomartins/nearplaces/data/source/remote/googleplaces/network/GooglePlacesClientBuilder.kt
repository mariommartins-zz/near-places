package com.mariomartins.nearplaces.data.source.remote.googleplaces.network

import com.mariomartins.nearplaces.BuildConfig
import com.mariomartins.nearplaces.data.source.remote.BaseClientBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object GooglePlacesClientBuilder : BaseClientBuilder<GooglePlacesApi>() {

    override val baseUrl: String get() = BuildConfig.GOOGLE_PLACES_URL

    override fun buildApiClient(): GooglePlacesApi =
        buildRetrofit().create(GooglePlacesApi::class.java)

    override fun buildMoshiAdapter(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
}
