package com.gigster.locationlist.data.apiservices

import android.content.res.Resources
import com.gigster.locationlist.data.model.APIResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIManager {
    lateinit var locationService: LocationService
    init {
        val builder = OkHttpClient().newBuilder()

        builder.addNetworkInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            val locale = Resources.getSystem().configuration.locale
            requestBuilder.addHeader("Accept-Language", locale.language)
            requestBuilder.addHeader("Accept", "application/json")
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        val client = builder.build()
        val BaseURL = "https://code-test-data.herokuapp.com/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        locationService = retrofit.create(LocationService::class.java)
    }

    fun getLocations(listenr: APIListener) {
        val call = locationService.getLocations()
        call.enqueue(object: Callback<APIResult> {
            override fun onResponse(call: Call<APIResult>, response: Response<APIResult>) {
                if (response.errorBody() == null) {
                    listenr.onSuccess(response.body())
                }
            }

            override fun onFailure(call: Call<APIResult>, t: Throwable) {

            }
        })
    }

    companion object {
        private var instance: APIManager? = null

        fun getInstance(): APIManager {
            if (instance == null) {
                instance = APIManager()
            }
            return instance!!
        }
    }
}