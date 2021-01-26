package com.mariomartins.nearplaces.data.source.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

abstract class BaseClientBuilder<T> {

    abstract val baseUrl: String

    abstract fun buildApiClient(): T

    protected fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(buildMoshiAdapter()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(buildOkHttpClient())
            .build()
    }

    private fun buildOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    abstract fun buildMoshiAdapter(): Moshi
}
