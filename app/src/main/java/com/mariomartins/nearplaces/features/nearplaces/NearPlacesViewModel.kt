package com.mariomartins.nearplaces.features.nearplaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.google.android.gms.maps.model.LatLng
import com.mariomartins.nearplaces.connectivity.ErrorHandlingViewModel
import com.mariomartins.nearplaces.domain.dispatchers.DispatcherMap
import com.mariomartins.nearplaces.domain.model.Place
import com.mariomartins.nearplaces.domain.model.enum.PlaceTypes.*
import com.mariomartins.nearplaces.domain.repository.IPlaceRepository
import com.mariomartins.nearplaces.extensions.map
import com.mariomartins.nearplaces.extensions.switchMap
import com.mariomartins.nearplaces.tools.SingleLiveEvent

class NearPlacesViewModel(
    private val repository: IPlaceRepository,
    dispatcherMap: DispatcherMap
) : ErrorHandlingViewModel(dispatcherMap) {

    private var nextPageToken: String? = null
    private var isNewSearch = true
    private var currentCategory = BAR
    var currentLocation: LatLng? = null
        private set

    val places: LiveData<PagedList<Place>> = buildPagedListLiveData()

    private val _requestPermissionEvent = SingleLiveEvent<Any>()
    val requestPermissionEvent: SingleLiveEvent<Any> get() = _requestPermissionEvent

    private val _locationPermissionDenied = MutableLiveData<Boolean>(false)
    val locationPermissionDenied: LiveData<Boolean> get() = _locationPermissionDenied

    private val _locationAnimationIsVisible = MutableLiveData<Boolean>(true)
    val locationAnimationIsVisible: LiveData<Boolean> get() = _locationAnimationIsVisible

    val emptyStateIsVisible: LiveData<Boolean> =
        _locationPermissionDenied.switchMap { locationDenied ->
            _progressIsVisible.switchMap { progress ->
                _locationAnimationIsVisible.switchMap { fetchingLocation ->
                    places.map {
                        !locationDenied && !progress && !fetchingLocation && it.isNullOrEmpty()
                    }
                }
            }
        }

    private val _progressIsVisible = MutableLiveData(false)
    val progressIsVisible: LiveData<Boolean> get() = _progressIsVisible

    private lateinit var pagingDataSource: PlacePagingDataSource

    fun onLocationChanged(latitude: Double, longitude: Double) {
        currentLocation = LatLng(latitude,longitude)
        _locationPermissionDenied.postValue(false)
        if (isNewSearch) pagingDataSource.invalidate()
    }

    fun onSwipeToRefresh() {
        if (_locationPermissionDenied.value == true) _requestPermissionEvent.postCall()
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

    fun onLocationPermissionDenied() {
        _locationAnimationIsVisible.postValue(false)
        _locationPermissionDenied.postValue(true)
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

            performRequestSafely(onError = { onPageError() }) {
                _progressIsVisible.postValue(true)

                isNewSearch = false
                load(currentLocation.latitude, currentLocation.longitude).let {
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

            performRequestSafely(onError = { onPageError() }) {
                load(currentLocation.latitude, currentLocation.longitude)
                    .let { callback.onResult(it, params.key + 1) }
            }
        }

        suspend fun load(latitude: Double, longitude: Double): List<Place> =
            repository.getPlacesBy(latitude, longitude, currentCategory.value, nextPageToken).let {
                nextPageToken = it.second
                it.first
            }

        private fun onPageError() {
            _progressIsVisible.postValue(false)
            _locationAnimationIsVisible.postValue(false)
        }
    }

    companion object {

        const val LIST_PAGE_LIMIT = 20
    }
}
