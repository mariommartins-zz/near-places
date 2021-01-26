package com.mariomartins.nearplaces.features.showplace

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.mariomartins.nearplaces.R
import com.mariomartins.nearplaces.databinding.DialogShowPlaceBinding


class ShowPlaceDialogFragment : DialogFragment() {

    private val arguments: ShowPlaceDialogFragmentArgs by navArgs()
    private lateinit var binding: DialogShowPlaceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogShowPlaceBinding
        .inflate(inflater)
        .apply {
            lifecycleOwner = viewLifecycleOwner
            isOpen = arguments.place.isOpen ?: false

            binding = this
        }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMap()
    }

    private fun setupMap() {
        val context = context ?: return
        val onMapReadyCallback = OnMapReadyCallback { googleMap ->
            MapsInitializer.initialize(context)
            googleMap.apply {
                addMarker(
                    MarkerOptions().position(arguments.userLocation)
                        .title(getString(R.string.show_place_user_marker_title))
                        .icon(bitmapDescriptorFromVector(context, R.drawable.icon_user))
                )
                arguments.place.latLng?.let {
                    addMarker(
                        MarkerOptions().position(it)
                            .title(arguments.place.name)
                            .icon(bitmapDescriptorFromVector(context, R.drawable.icon_location))
                    ).showInfoWindow()
                    updateCamera(this, it)
                }
            }
        }
        binding.showPlaceMap.apply {
            onCreate(null)
            onResume()
            getMapAsync(onMapReadyCallback)
        }
    }

    private fun updateCamera(map: GoogleMap, placeLocation: LatLng) {
        val builder = LatLngBounds.Builder()
        builder.include(placeLocation)
        builder.include(arguments.userLocation)

        val bounds = builder.build()
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, DEFAULT_CAMERA_PADDING)
        map.animateCamera(cameraUpdate)
    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        @DrawableRes vectorResId: Int
    ): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
        vectorDrawable.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {

        private const val DEFAULT_CAMERA_PADDING = 150
    }
}