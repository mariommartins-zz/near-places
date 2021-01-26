package com.mariomartins.nearplaces.data

import com.mariomartins.nearplaces.NearPlacesApplication.Companion.GOOGLE_API_KEY_SCOPE
import com.mariomartins.nearplaces.data.source.local.cache.PlaceCache
import com.mariomartins.nearplaces.data.source.remote.googleplaces.IPlacesRemoteDataSource
import com.mariomartins.nearplaces.data.source.remote.googleplaces.PlacesRemoteDataSource
import com.mariomartins.nearplaces.data.source.remote.googleplaces.network.GooglePlacesClientBuilder
import com.mariomartins.nearplaces.domain.model.mapper.PlaceMapper
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single { GooglePlacesClientBuilder.buildApiClient() }

    factory { GooglePlacesClientBuilder.buildMoshiAdapter() }
}

val dataSourceModule = module {
    single { PlaceCache() }

    single<IPlacesRemoteDataSource> {
        PlacesRemoteDataSource(
            api = get(),
            apiKey = get(named(GOOGLE_API_KEY_SCOPE)),
            mapper = get<PlaceMapper>(),
            cache = get()
        )
    }
}

val dataModule = networkModule + dataSourceModule