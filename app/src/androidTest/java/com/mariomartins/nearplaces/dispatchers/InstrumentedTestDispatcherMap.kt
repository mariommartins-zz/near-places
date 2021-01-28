package com.mariomartins.nearplaces.dispatchers

import com.mariomartins.nearplaces.domain.dispatchers.DispatcherMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object InstrumentedTestDispatcherMap : DispatcherMap {

    override val io: CoroutineDispatcher = Dispatchers.Unconfined
    override val ui: CoroutineDispatcher = Dispatchers.Main
    override val computation: CoroutineDispatcher = Dispatchers.Unconfined
}
