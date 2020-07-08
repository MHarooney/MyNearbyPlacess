package com.example.chamatestapp.Remote

import com.example.chamatestapp.Model.MyPlaces
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface IGoogleAPIService {
    @GET
    fun getNearByPlaces(@Url url: String?): Call<MyPlaces?>?
}