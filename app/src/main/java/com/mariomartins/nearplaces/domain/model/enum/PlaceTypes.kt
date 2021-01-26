package com.mariomartins.nearplaces.domain.model.enum

enum class PlaceTypes(val value: String) {
    BAR("bar"),
    CAFE("cafe"),
    RESTAURANT("restaurant");

    override fun toString() = value
}