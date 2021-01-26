package com.mariomartins.nearplaces.data.source.local.cache

import com.mariomartins.nearplaces.data.source.remote.googleplaces.response.PlaceResponse
import com.mariomartins.nearplaces.data.source.remote.googleplaces.response.ResponseWrapper

class PlaceCache : MapCache<String, ResponseWrapper<PlaceResponse>> {

    override var id: String = CACHE_DEFAULT_ID

    private val items: MutableMap<String, ResponseWrapper<PlaceResponse>> = HashMap()

    override fun get(key: String) = items[key]

    override fun set(key: String, value: ResponseWrapper<PlaceResponse>) {
        items[key] = value
    }

    override fun minusAssign(key: String) {
        items.remove(key)
    }

    override fun clear() = items.clear()

    companion object {

        private const val CACHE_DEFAULT_ID = "CACHE_DEFAULT_ID"
    }
}
