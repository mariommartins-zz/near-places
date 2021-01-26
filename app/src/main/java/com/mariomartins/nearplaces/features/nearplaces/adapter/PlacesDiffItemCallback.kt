package com.mariomartins.nearplaces.features.nearplaces.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mariomartins.nearplaces.domain.model.Place

object PlacesDiffItemCallback : DiffUtil.ItemCallback<Place>() {

    override fun areItemsTheSame(oldItem: Place, newItem: Place) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Place, newItem: Place) = oldItem.equals(newItem)
}