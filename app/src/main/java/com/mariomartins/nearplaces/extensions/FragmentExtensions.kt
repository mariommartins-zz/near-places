package com.mariomartins.nearplaces.extensions

import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.mariomartins.nearplaces.R

fun Fragment.showWarningSnackbar(anchorView: View, @StringRes actionTextRes: Int) =
    showSnackbar(
        anchorView,
        resources.getString(actionTextRes),
        context?.let { ContextCompat.getDrawable(it, R.drawable.shape_warning_snackbar_background) }
    )

private fun showSnackbar(anchorView: View, actionText: String, background: Drawable?) =
    Snackbar
        .make(anchorView, actionText, Snackbar.LENGTH_LONG)
        .apply {
            setActionTextColor(ContextCompat.getColor(context, R.color.black))
            view.background = background
            show()
        }

fun Fragment.getScreenWidth() = DisplayMetrics().let { displayMetrics ->
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
        activity?.display?.getRealMetrics(displayMetrics)
    else {
        @Suppress("DEPRECATION")
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
    }

    displayMetrics.widthPixels
}