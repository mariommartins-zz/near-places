package com.mariomartins.nearplaces.extensions

import androidx.recyclerview.widget.RecyclerView
import org.koin.core.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

inline fun <reified T> RecyclerView.ViewHolder.get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T = when (this) {
    is KoinComponent -> getKoin().get(qualifier, parameters)
    else -> GlobalContext.get().koin.get(qualifier, parameters)
}