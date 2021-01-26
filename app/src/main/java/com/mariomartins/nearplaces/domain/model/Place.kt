package com.mariomartins.nearplaces.domain.model

data class Place(
    val id: String,
    val name: String?,
    val imageUrl: String?,
    val isOpen: Boolean?,
    val rating: Double?
)
