package com.gigster.locationlist.data.apiservices

import com.gigster.locationlist.data.model.APIResult

interface APIListener {
    fun onSuccess(apiResult: APIResult?)
}