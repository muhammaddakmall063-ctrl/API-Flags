package com.example.apiflags.data


data class CountryResponse(
    val error: Boolean,
    val msg: String,
    val data: List<Country>
)