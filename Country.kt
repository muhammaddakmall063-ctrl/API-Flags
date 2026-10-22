package com.example.apiflags.data

import java.io.Serializable

data class Country(
    val name: String,
    val iso2: String,
    val iso3: String,
    val flag: String
) : Serializable