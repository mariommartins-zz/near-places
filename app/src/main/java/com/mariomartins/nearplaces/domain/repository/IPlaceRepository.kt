package com.mariomartins.nearplaces.domain.repository

import com.mariomartins.nearplaces.domain.model.Place

interface IPlaceRepository {

    suspend fun getPlacesBy(
        latitude: Double,
        longitude: Double,
        type: String,
        nextPageToken: String?
    ): Pair<List<Place>, String?>
}
