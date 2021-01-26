package com.mariomartins.nearplaces.features.nearplaces.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import com.mariomartins.nearplaces.databinding.ItemPlaceBinding
import com.mariomartins.nearplaces.domain.model.Place

class PlacesPagedAdapter(private val lifecycleOwner: LifecycleOwner) :
    PagedListAdapter<Place, PlaceViewHolder>(PlacesDiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlaceViewHolder(
            ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            lifecycleOwner
        )

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    override fun onViewRecycled(holder: PlaceViewHolder) = holder.recycle()
}