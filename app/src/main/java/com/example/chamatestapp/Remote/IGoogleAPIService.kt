package com.example.chamatestapp.Remote

import com.example.chamatestapp.Model.MyPlaces
import com.example.chamatestapp.Model.PlaceDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface IGoogleAPIService {
    @GET
    fun getNearByPlaces(@Url url: String?): Call<MyPlaces?>?

    @GET
    fun getDetailPlace(@Url url: String?): Call<PlaceDetail?>?

    @GET("maps/api/directions/json")
    fun getDirections(
        @Query("origin") origin: String?,
        @Query("destination") destination: String?
    ): Call<String?>?
}