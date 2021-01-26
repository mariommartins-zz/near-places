package com.mariomartins.nearplaces.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <T, R> LiveData<T>.map(mapFunction: (T) -> R): LiveData<R> =
    Transformations.map(this, mapFunction)

fun <T, R> LiveData<T>.switchMap(switchMap: (T) -> LiveData<R>?): LiveData<R> =
    Transformations.switchMap(this, switchMap)