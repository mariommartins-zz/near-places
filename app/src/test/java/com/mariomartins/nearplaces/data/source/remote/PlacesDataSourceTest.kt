package com.mariomartins.nearplaces.data.source.remote

import com.mariomartins.nearplaces.data.source.local.cache.PlaceCache
import com.mariomartins.nearplaces.data.source.remote.googleplaces.IPlacesRemoteDataSource
import com.mariomartins.nearplaces.data.source.remote.googleplaces.PlacesRemoteDataSource
import com.mariomartins.nearplaces.data.source.remote.googleplaces.network.GooglePlacesApi
import com.mariomartins.nearplaces.domain.model.enum.PlaceTypes
import com.mariomartins.nearplaces.domain.model.mapper.PlaceMapper
import com.mariomartins.nearplaces.features.nearplaces.NearPlacesViewModel.Companion.LIST_PAGE_LIMIT
import com.mariomartins.nearplaces.unitmocks.mockPlaces
import com.mariomartins.nearplaces.unitmocks.mockPlacesResponse
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class PlacesDataSourceTest {

    @Mock
    private lateinit var api: GooglePlacesApi

    private val mockedPlacesResponse = mockPlacesResponse(LIST_PAGE_LIMIT)
    private val mockedEmptyPlaceListResponse = mockPlacesResponse(0)
    private val mockedPlaces = mockPlaces(LIST_PAGE_LIMIT)

    private lateinit var dataSource: IPlacesRemoteDataSource
    private val mapper = PlaceMapper()
    private val cache = PlaceCache()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        dataSource = PlacesRemoteDataSource(api, "", mapper, cache)
    }

    @Test
    fun `test fetchPlaces when api-fetchPlacesAsync returns empty list`() = runBlocking<Unit> {
        whenever(api.fetchPlacesAsync(any(), any(), any(), any(), any()))
            .thenReturn(mockedEmptyPlaceListResponse)

        val result = dataSource.fetchPlaces(
            latitude = 0.0,
            longitude = 0.0,
            type = PlaceTypes.BAR.value,
            nextPageToken = ""
        )

        assertEquals(0, result.first.size)
        verify(api, times(1)).fetchPlacesAsync(any(), any(), any(), any(), any())
    }

    @Test
    fun `test fetchPlaces when api-fetchPlacesAsync returns data`() = runBlocking<Unit> {
        whenever(api.fetchPlacesAsync(any(), any(), any(), any(), any()))
            .thenReturn(mockedPlacesResponse)

        val result = dataSource.fetchPlaces(
            latitude = 0.0,
            longitude = 0.0,
            type = PlaceTypes.BAR.value,
            nextPageToken = ""
        )

        assertEquals(mockedPlaces, result.first)
        verify(api, times(1)).fetchPlacesAsync(any(), any(), any(), any(), any())
    }
}
