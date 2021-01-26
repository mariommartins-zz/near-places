package com.mariomartins.nearplaces.features.nearplaces

import com.mariomartins.nearplaces.domain.model.Place
import com.mariomartins.nearplaces.features.nearplaces.adapter.PlaceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val nearPlacesModule = module {
    viewModel {
        NearPlacesViewModel(
            repository = get(),
            dispatcherMap = get()
        )
    }

    factory { (place: Place) -> PlaceViewModel(place = place) }
}
