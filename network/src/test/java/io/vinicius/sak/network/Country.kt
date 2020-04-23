package io.vinicius.sak.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Country
(
    val alpha2Code: String? = null,
    val name: String? = null,
    val capital: String? = null
)