package com.mariomartins.nearplaces.domain.model.mapper

interface ModelMapper<in I, out O> {
    fun make(input: I): O?
}
