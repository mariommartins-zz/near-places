package com.mariomartins.nearplaces.searchrv

import androidx.test.core.app.ActivityScenario
import com.mariomartins.nearplaces.data.source.remote.googleplaces.network.GooglePlacesApi
import com.mariomartins.nearplaces.dispatchers.InstrumentedTestDispatcherMap
import com.mariomartins.nearplaces.features.main.MainActivity
import com.mariomartins.nearplaces.features.nearplaces.NearPlacesViewModel.Companion.LIST_PAGE_LIMIT
import com.mariomartins.nearplaces.instrumentedmocks.mockPlacesResponse
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class NearPlacesTest {

    @Mock
    private lateinit var api: GooglePlacesApi

    private val mockedPlacesResponse = mockPlacesResponse(LIST_PAGE_LIMIT)
    private val mockedEmptyPlaceListResponse = mockPlacesResponse(0)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        module(override = true) {
            single { api }

            single { InstrumentedTestDispatcherMap }
        }.also { loadKoinModules(it) }
    }

    @Test
    fun test_near_places_screen_for_api_for_empty_results() = nearPlacesState {
        whenever(api.fetchPlacesAsync(any(), any(), any(), any(), any()))
            .thenReturn(mockedEmptyPlaceListResponse)

        ActivityScenario.launch(MainActivity::class.java)

        delay(DELAY_TO_LOAD_API_RESPONSE)

        matchEmptyStateIsVisible()

        matchAmountOfListedItems(0)
    }

    @Test
    fun test_near_places_screen_for_api_results() = nearPlacesState {
        whenever(api.fetchPlacesAsync(any(), any(), any(), any(), any()))
            .thenReturn(mockedPlacesResponse)

        ActivityScenario.launch(MainActivity::class.java)

        delay(DELAY_TO_LOAD_API_RESPONSE)

        matchEmptyStateIsGone()

        matchAmountOfListedItems(LIST_PAGE_LIMIT * 2)
    }

    companion object {

        private const val DELAY_TO_LOAD_API_RESPONSE = 2000L
    }
}