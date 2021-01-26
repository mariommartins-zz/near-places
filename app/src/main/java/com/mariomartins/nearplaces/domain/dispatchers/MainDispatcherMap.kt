package com.mariomartins.nearplaces.domain.dispatchers

import com.mariomartins.nearplaces.domain.dispatchers.DispatcherMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MainDispatcherMap : DispatcherMap {

    override val io: CoroutineDispatcher = Dispatchers.IO
    override val ui: CoroutineDispatcher = Dispatchers.Main
    override val computation: CoroutineDispatcher = Dispatchers.Default
}
