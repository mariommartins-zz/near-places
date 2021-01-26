package com.mariomartins.nearplaces.domain.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Place(
    val id: String,
    val name: String?,
    val imageUrl: String?,
    val isOpen: Boolean?,
    val rating: Double?,
    val latLng: LatLng?
) : Parcelable
