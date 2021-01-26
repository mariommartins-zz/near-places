package com.mariomartins.nearplaces.features.nearplaces.adapter

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.mariomartins.nearplaces.databinding.ItemPlaceBinding
import com.mariomartins.nearplaces.domain.model.Place
import com.mariomartins.nearplaces.extensions.get
import org.koin.core.parameter.parametersOf

class PlaceViewHolder(
    private val binding: ItemPlaceBinding,
    lifecycleOwner: LifecycleOwner
) : RecyclerView.ViewHolder(binding.root) {

    private var viewModel: PlaceViewModel? = null

    init {
        binding.lifecycleOwner = lifecycleOwner
    }

    fun bind(item: Place) {
        viewModel = get<PlaceViewModel> { parametersOf(item) }.apply { binding.viewModel = this }
    }

    fun recycle() {
        viewModel = null
    }
}