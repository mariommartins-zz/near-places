package com.mariomartins.nearplaces.connectivity

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.NavController
import com.mariomartins.nearplaces.R
import com.mariomartins.nearplaces.extensions.showWarningSnackbar

abstract class ErrorHandlingFragment : Fragment() {

    protected abstract val binding: ViewDataBinding
    protected abstract val viewModel: ErrorHandlingViewModel
    protected abstract val navController: NavController

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeEvents()

        doOnViewCreated(view, savedInstanceState)
    }

    private fun observeEvents() = with(viewModel) {
        networkErrorEvent.observe(viewLifecycleOwner) {
            showWarningSnackbar(binding.root, R.string.network_error_text)
        }

        genericErrorEvent.observe(viewLifecycleOwner) {
            showWarningSnackbar(binding.root, R.string.generic_error_text)
        }
    }

    protected open fun doOnViewCreated(view: View, savedInstanceState: Bundle?) = Unit
}
