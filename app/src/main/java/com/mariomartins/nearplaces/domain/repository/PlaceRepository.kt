package com.mariomartins.nearplaces.domain.repository

import com.mariomartins.nearplaces.data.source.remote.googleplaces.IPlacesRemoteDataSource

class PlaceRepository(
    private val remoteDataSource: IPlacesRemoteDataSource
) : IPlaceRepository {

    override suspend fun getPlacesBy(
        latitude: Double,
        longitude: Double,
        type: String,
        nextPageToken: String?
    ) = remoteDataSource.fetchPlaces(latitude, longitude, type, nextPageToken)
}
