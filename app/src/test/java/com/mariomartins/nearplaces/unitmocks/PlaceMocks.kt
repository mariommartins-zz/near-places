package com.mariomartins.nearplaces.unitmocks

import com.google.android.gms.maps.model.LatLng
import com.mariomartins.nearplaces.data.source.remote.googleplaces.response.*
import com.mariomartins.nearplaces.domain.model.Place
import kotlinx.coroutines.CompletableDeferred

fun mockPlacesResponse(size: Int) =
    CompletableDeferred(
        ResponseWrapper(
            nextPageToken = "nextPageToken",
            results = mutableListOf<PlaceResponse>()
                .apply { repeat(size) { this += mockPlaceResponse(placeId = "$it") } },
            status = "status"
            )
    )

private fun mockPlaceResponse(
    placeId: String? = "placeId",
    name: String? = "name",
    icon: String? = "imageUrl",
    rating: Double? = 0.0,
    types: List<String>? = emptyList(),
) =
    PlaceResponse(
        geometry = mockPlaceResponseGeometry(),
        name = name,
        icon = icon,
        placeId = placeId,
        openingHours = mockPlaceResponseOpenStatus(),
        rating = rating,
        types = types
    )

private fun mockPlaceResponseOpenStatus() =
    PlaceResponseOpenStatus(false)

private fun mockPlaceResponseGeometry() =
    PlaceResponseGeometry(mockPlaceResponseLocation())

private fun mockPlaceResponseLocation(lat: Double? = -27.54657215, lng: Double? = -48.49617407) =
    PlaceResponseLocation(lat, lng)

fun mockPlaces(size: Int): List<Place> =
    mutableListOf<Place>().apply { repeat(size) { this += mockPlace(id = "$it") } }

private fun mockPlace(
    id: String = "id",
    name: String? = "name",
    imageUrl: String? = "imageUrl",
    isOpen: Boolean? = false,
    rating: Double? = 0.0,
    latLng: LatLng? = LatLng(-27.54657215, -48.49617407)
) =
    Place(
        id = id,
        name = name,
        imageUrl = imageUrl,
        isOpen = isOpen,
        rating = rating,
        latLng = latLng
    )
