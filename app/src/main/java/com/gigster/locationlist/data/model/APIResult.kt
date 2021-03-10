package com.gigster.locationlist.data.model

data class APIResult(
    val id: String,
    val resourceType: String,
    val total: Int,
    val entry: List<Entry>
)
