package com.mariomartins.nearplaces.data.source.remote.googleplaces

import com.mariomartins.nearplaces.domain.model.Place

interface IPlacesRemoteDataSource {

    suspend fun fetchPlaces(
        latitude: Double,
        longitude: Double,
        type: String,
        nextPageToken: String?
    ): Pair<List<Place>, String?>
}
