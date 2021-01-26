package com.mariomartins.nearplaces.features.nearplaces.adapter

import androidx.lifecycle.ViewModel
import com.mariomartins.nearplaces.domain.model.Place
import com.mariomartins.nearplaces.tools.SingleLiveEvent

class PlaceViewModel(place: Place) : ViewModel() {

    val name = place.name
    val imageUrl = place.imageUrl
    val isOpen = place.isOpen
    val rating = place.rating?.toInt() ?: 0

    val noRating = place.rating == null
    val hasLocationContent = place.latLng != null

    private val _onItemClickedEvent = SingleLiveEvent<Any>()
    val onItemClickedEvent: SingleLiveEvent<Any> get() = _onItemClickedEvent

    fun onItemClicked() = _onItemClickedEvent.postCall()
}
