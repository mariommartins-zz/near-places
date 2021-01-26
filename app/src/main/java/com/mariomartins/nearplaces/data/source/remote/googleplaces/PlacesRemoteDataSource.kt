package com.mariomartins.nearplaces.data.source.remote.googleplaces

import com.mariomartins.nearplaces.data.source.local.cache.PlaceCache
import com.mariomartins.nearplaces.data.source.remote.googleplaces.network.GooglePlacesApi
import com.mariomartins.nearplaces.data.source.remote.googleplaces.response.PlaceResponse
import com.mariomartins.nearplaces.data.source.remote.googleplaces.response.ResponseWrapper
import com.mariomartins.nearplaces.domain.model.Place
import com.mariomartins.nearplaces.domain.model.mapper.PlaceMapperAlias

class PlacesRemoteDataSource(
    private val api: GooglePlacesApi,
    private val apiKey: String,
    private val mapper: PlaceMapperAlias,
    private val cache: PlaceCache
) : IPlacesRemoteDataSource {

    override suspend fun fetchPlaces(
        latitude: Double,
        longitude: Double,
        type: String,
        nextPageToken: String?
    ): Pair<List<Place>, String?> {
        var cachedResponse: ResponseWrapper<PlaceResponse>? = null
        val location = "$latitude,$longitude"
        val cacheKey = "$type,$nextPageToken"

        if (cache.id != location) {
            cache.id = location
            cache.clear()
        } else if (cache[cacheKey] != null) cachedResponse = cache[cacheKey]

        val result =
            cachedResponse ?: api.fetchPlacesAsync(
                location = location,
                type = type,
                pageToken = nextPageToken,
                key = apiKey
            ).await().also { cache[cacheKey] = it }

        return result.let {
            it.results.mapNotNull { response -> mapper.make(response) } to it.nextPageToken
        }
    }
}
