package com.mariomartins.nearplaces.features.nearplaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PageKeyedDataSource
import com.mariomartins.nearplaces.connectivity.ErrorHandlingViewModel
import com.mariomartins.nearplaces.domain.dispatchers.DispatcherMap
import com.mariomartins.nearplaces.domain.model.Place
import com.mariomartins.nearplaces.domain.model.enum.PlaceTypes.*
import com.mariomartins.nearplaces.domain.repository.IPlaceRepository
import com.mariomartins.nearplaces.extensions.map
import com.mariomartins.nearplaces.extensions.switchMap
import java.lang.IllegalArgumentException

class NearPlacesViewModel(
    private val repository: IPlaceRepository,
    dispatcherMap: DispatcherMap
) : ErrorHandlingViewModel(dispatcherMap) {

    private var currentLocation: Pair<Double, Double>? = null
    private var nextPageToken: String? = null
    private var isNewSearch = true
    private var currentCategory = BAR

    val places: LiveData<PagedList<Place>> = buildPagedListLiveData()

    private val _locationAnimationIsVisible = MutableLiveData<Boolean>(true)
    val locationAnimationIsVisible: LiveData<Boolean> get() = _locationAnimationIsVisible

    val emptyStateIsVisible: LiveData<Boolean>
        get() = _progressIsVisible.switchMap { progress ->
            _locationAnimationIsVisible.switchMap { fetchingLocation ->
                places.map { !progress && !fetchingLocation && it.isNullOrEmpty() }
            }
        }

    private val _progressIsVisible = MutableLiveData(false)
    val progressIsVisible: LiveData<Boolean> get() = _progressIsVisible

    private lateinit var pagingDataSource: PlacePagingDataSource

    fun onLocationChanged(latitude: Double, longitude: Double) {
        currentLocation = latitude to longitude
        if (isNewSearch) pagingDataSource.invalidate()
    }

    fun onSwipeToRefresh() {
        isNewSearch = true
        nextPageToken = null
        pagingDataSource.invalidate()
    }

    fun onCategorySelected(position: Int) {
        currentCategory = when (position) {
            0 -> BAR
            1 -> CAFE
            2 -> RESTAURANT
            else -> throw IllegalArgumentException()
        }
        isNewSearch = true
        nextPageToken = null
        pagingDataSource.invalidate()
    }

    private fun buildPagedListLiveData() =
        LivePagedListBuilder(
            object : DataSource.Factory<Int, Place>() {
                override fun create() = PlacePagingDataSource().also { pagingDataSource = it }
            },
            Config(
                LIST_PAGE_LIMIT,
                enablePlaceholders = false,
                initialLoadSizeHint = LIST_PAGE_LIMIT
            )
        ).build()

    private inner class PlacePagingDataSource : PageKeyedDataSource<Int, Place>() {
        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Place>) =
            Unit

        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, Place>
        ) {
            val currentLocation = currentLocation ?: run {
                callback.onResult(emptyList(), null, 0)
                return
            }
            val latitude = currentLocation.first
            val longitude = currentLocation.second

            performRequestSafely(onError = { onPageError() }) {
                _progressIsVisible.postValue(true)

                isNewSearch = false
                load(latitude, longitude).let {
                    callback.onResult(it, null, 1)
                    _locationAnimationIsVisible.postValue(false)
                }

                _progressIsVisible.postValue(false)
            }
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Place>) {
            val currentLocation = currentLocation ?: run {
                callback.onResult(emptyList(), null)
                return
            }
            if (nextPageToken == null) callback.onResult(emptyList(), null)

            val latitude = currentLocation.first
            val longitude = currentLocation.second

            performRequestSafely(onError = { onPageError() }) {
                load(latitude, longitude)
                    .let { callback.onResult(it, params.key + 1) }
            }
        }

        suspend fun load(latitude: Double, longitude: Double): List<Place> =
            repository.getPlacesBy(latitude, longitude, currentCategory.value, nextPageToken).let {
                nextPageToken = it.second
                it.first
            }

        private fun onPageError() = _progressIsVisible.postValue(false)
    }

    companion object {

        const val LIST_PAGE_LIMIT = 20
    }
}
