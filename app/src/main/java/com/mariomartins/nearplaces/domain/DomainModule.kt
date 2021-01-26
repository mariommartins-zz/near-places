package com.mariomartins.nearplaces.domain

import com.mariomartins.nearplaces.domain.dispatchers.DispatcherMap
import com.mariomartins.nearplaces.domain.dispatchers.MainDispatcherMap
import com.mariomartins.nearplaces.domain.model.mapper.PlaceMapper
import com.mariomartins.nearplaces.domain.repository.IPlaceRepository
import com.mariomartins.nearplaces.domain.repository.PlaceRepository
import org.koin.dsl.module

val dispatchersModule = module {
    single<DispatcherMap> { MainDispatcherMap() }
}

val mapperModule = module {
    single { PlaceMapper() }
}

val repositoryModule = module {
    single<IPlaceRepository> { PlaceRepository(remoteDataSource = get()) }
}

val domainModule = listOf(
    dispatchersModule,
    mapperModule,
    repositoryModule
)
