package com.mariomartins.nearplaces.domain.model.mapper

import com.mariomartins.nearplaces.data.source.remote.googleplaces.response.PlaceResponse
import com.mariomartins.nearplaces.domain.model.Place

typealias PlaceMapperAlias = ModelMapper<PlaceResponse, Place?>

class PlaceMapper : PlaceMapperAlias {
    override fun make(input: PlaceResponse): Place? {
        return Place(
            id = input.placeId ?: return null,
            name = input.name ?: return null,
            imageUrl = input.icon,
            isOpen = input.openingHours?.openNow ?: false,
            rating = input.rating
        )
    }
}
