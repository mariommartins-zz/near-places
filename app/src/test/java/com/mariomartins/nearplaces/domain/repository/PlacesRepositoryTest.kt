package com.mariomartins.nearplaces.domain.repository

import com.mariomartins.nearplaces.data.source.remote.googleplaces.IPlacesRemoteDataSource
import com.mariomartins.nearplaces.domain.model.enum.PlaceTypes
import com.mariomartins.nearplaces.features.nearplaces.NearPlacesViewModel.Companion.LIST_PAGE_LIMIT
import com.mariomartins.nearplaces.unitmocks.mockPlaces
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

class PlacesRepositoryTest {

    @Mock
    private lateinit var remoteDataSource: IPlacesRemoteDataSource

    private val mockedVehicles = mockPlaces(LIST_PAGE_LIMIT)

    private lateinit var repository: IPlaceRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        repository = PlaceRepository(remoteDataSource)
    }

    @Test
    fun `test getPlacesBy when dataSource-fetchPlaces returns emptyList`() = runBlocking<Unit> {
        whenever(remoteDataSource.fetchPlaces(any(), any(), any(), any()))
            .thenReturn(Pair(emptyList(), ""))

        val result = repository.getPlacesBy(
            latitude = 0.0,
            longitude = 0.0,
            type = PlaceTypes.BAR.value,
            nextPageToken = ""
        )

        assertEquals(0, result.first.size)
        verify(remoteDataSource, times(1)).fetchPlaces(any(), any(), any(), any())
    }

    @Test
    fun `test getPlacesBy when dataSource-fetchPlaces returns data`() = runBlocking<Unit> {
        whenever(remoteDataSource.fetchPlaces(any(), any(), any(), any()))
            .thenReturn(Pair(mockedVehicles, ""))

        val result = repository.getPlacesBy(
            latitude = 0.0,
            longitude = 0.0,
            type = PlaceTypes.BAR.value,
            nextPageToken = ""
        )

        assertEquals(mockedVehicles, result.first)
        verify(remoteDataSource, times(1)).fetchPlaces(any(), any(), any(), any())
    }
}
