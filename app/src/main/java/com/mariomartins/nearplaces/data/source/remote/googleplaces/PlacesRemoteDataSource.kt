package com.mariomartins.nearplaces.data.source.remote.googleplaces

import com.mariomartins.nearplaces.data.source.remote.googleplaces.network.GooglePlacesApi
import com.mariomartins.nearplaces.domain.model.mapper.PlaceMapperAlias

class PlacesRemoteDataSource(
    private val api: GooglePlacesApi,
    private val mapper: PlaceMapperAlias
) : IPlacesRemoteDataSource {

    override suspend fun fetchPlaces(
        latitude: Double,
        longitude: Double,
        type: String,
        nextPageToken: String?
    ) = api
        .fetchPlacesAsync(
            location = "$latitude,$longitude",
            type = type,
            pageToken = nextPageToken
        )
        .await()
        .let { it.results.mapNotNull { response -> mapper.make(response) } to it.nextPageToken }
}
