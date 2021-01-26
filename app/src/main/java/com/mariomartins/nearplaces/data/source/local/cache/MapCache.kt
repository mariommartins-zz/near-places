package com.mariomartins.nearplaces.data.source.local.cache

interface MapCache<in K, V> {
    var id: String

    operator fun get(key: K): V?

    operator fun set(key: K, value: V)

    operator fun minusAssign(key: K)

    fun clear()
}