package com.mariomartins.nearplaces.features.nearplaces

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.R.layout.simple_spinner_dropdown_item
import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.mariomartins.nearplaces.R
import com.mariomartins.nearplaces.connectivity.ErrorHandlingFragment
import com.mariomartins.nearplaces.databinding.FragmentNearPlacesBinding
import com.mariomartins.nearplaces.extensions.showWarningSnackbar
import com.mariomartins.nearplaces.features.nearplaces.adapter.PlacesPagedAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class NearPlacesFragment : ErrorHandlingFragment(), LocationListener {

    override lateinit var binding: FragmentNearPlacesBinding
    override val viewModel: NearPlacesViewModel by viewModel()
    override val navController by lazy { findNavController() }

    private lateinit var locationManager: LocationManager

    private lateinit var placesPagedAdapter: PlacesPagedAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (accessLocationPermissionStatus(context)) setupLocationProviderClient(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentNearPlacesBinding.inflate(inflater).apply {
        lifecycleOwner = viewLifecycleOwner
        viewModel = this@NearPlacesFragment.viewModel

        binding = this
    }.root

    override fun doOnViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
        observeEvents()
    }

    private fun setupViews() = with(binding) {
        nearPlacesCategoriesSp.apply {
            val categoryArray = resources.getStringArray(R.array.near_places_category_array)
            val arrayAdapter = ArrayAdapter(context, simple_spinner_dropdown_item, categoryArray)
            arrayAdapter.setDropDownViewResource(simple_spinner_dropdown_item)
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) = Unit

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel?.onCategorySelected(position)
                }
            }
            adapter = arrayAdapter
        }

        nearPlacesResultsSrl.apply {
            setOnRefreshListener { this@NearPlacesFragment.viewModel.onSwipeToRefresh() }
            setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary)
            layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        }

        placesPagedAdapter = PlacesPagedAdapter(viewLifecycleOwner)
        nearPlacesResultsRv.adapter = placesPagedAdapter
    }

    private fun observeEvents() {
        viewModel.places.observe(viewLifecycleOwner) { placesPagedAdapter.submitList(it) }

        viewModel.progressIsVisible.observe(viewLifecycleOwner) {
            binding.nearPlacesResultsSrl.isRefreshing = it
        }
    }

    private fun accessLocationPermissionStatus(context: Context): Boolean =
        if (checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED &&
            checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
        ) true
        else {
            requestPermissions(
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            false
        }

    override fun onLocationChanged(location: Location) {
        viewModel.onLocationChanged(location.latitude, location.longitude)
    }

    private fun showSecondChangePositionRequest() {
        val context = context ?: return
        AlertDialog.Builder(context)
            .setMessage(R.string.near_places_loaction_dialog_body)
            .setPositiveButton(R.string.near_places_dialog_yes) { _, _ -> accessLocationPermissionStatus(context) }
            .setNeutralButton(R.string.near_places_dialog_cancel) { _, _ ->
                showWarningSnackbar(binding.root, R.string.location_error_text)
            }
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED)
                    context?.let { setupLocationProviderClient(it) }
                else showSecondChangePositionRequest()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupLocationProviderClient(context: Context) {
        if (accessLocationPermissionStatus(context)) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let { it: Location ->
                    viewModel.onLocationChanged(it.latitude, it.longitude)
                } ?: startGpsLocationTracker(context)
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun startGpsLocationTracker(context: Context) {
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (gpsEnabled) {
            if (accessLocationPermissionStatus(context)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_MANAGER_MIN_TIME,
                    LOCATION_MANAGER_MIN_DISTANCE,
                    this
                )
            }
        } else
            AlertDialog.Builder(context)
                .setTitle(R.string.near_places_gps_dialog_title)
                .setMessage(R.string.near_places_gps_dialog_body)
                .setPositiveButton(R.string.near_places_dialog_yes) { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNeutralButton(R.string.near_places_dialog_cancel, null)
                .create()
                .show()
    }

    companion object {

        private const val LOCATION_REQUEST_CODE = 100

        private const val LOCATION_MANAGER_MIN_TIME = 10L
        private const val LOCATION_MANAGER_MIN_DISTANCE = 10F
    }
}
