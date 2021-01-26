package com.mariomartins.nearplaces.data

import com.mariomartins.nearplaces.data.source.remote.googleplaces.IPlacesRemoteDataSource
import com.mariomartins.nearplaces.data.source.remote.googleplaces.PlacesRemoteDataSource
import com.mariomartins.nearplaces.data.source.remote.googleplaces.network.GooglePlacesClientBuilder
import com.mariomartins.nearplaces.domain.model.mapper.PlaceMapper
import org.koin.dsl.module

val networkModule = module {
    single { GooglePlacesClientBuilder.buildApiClient() }

    factory { GooglePlacesClientBuilder.buildMoshiAdapter() }
}

val dataSourceModule = module {
    single<IPlacesRemoteDataSource> {
        PlacesRemoteDataSource(
            api = get(),
            mapper = get<PlaceMapper>()
        )
    }
}

val dataModule = networkModule + dataSourceModule