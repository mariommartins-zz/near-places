package com.mariomartins.nearplaces.features.nearplaces.adapter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.mariomartins.nearplaces.databinding.ItemPlaceBinding
import com.mariomartins.nearplaces.domain.model.Place
import com.mariomartins.nearplaces.extensions.get
import org.koin.core.parameter.parametersOf

class PlaceViewHolder(
    private val binding: ItemPlaceBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val onItemClicked: (place: Place) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var viewModel: PlaceViewModel? = null

    init {
        binding.lifecycleOwner = lifecycleOwner
    }

    fun bind(item: Place) {
        viewModel = get<PlaceViewModel> { parametersOf(item) }.apply { binding.viewModel = this }

        observeEvents(item)
    }

    private fun observeEvents(item: Place) {
        viewModel?.onItemClickedEvent?.observe(lifecycleOwner) {
            onItemClicked(item)
        }
    }

    fun recycle() {
        viewModel = null
    }
}