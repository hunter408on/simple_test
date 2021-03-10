package com.gigster.locationlist.data.apiservices

import com.gigster.locationlist.data.model.APIResult
import retrofit2.Call
import retrofit2.http.GET
interface LocationService {
    @GET("/locations")
    fun getLocations(): Call<APIResult>
}