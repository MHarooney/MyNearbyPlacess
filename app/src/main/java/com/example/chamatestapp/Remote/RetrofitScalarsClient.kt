package com.example.chamatestapp.Remote

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitScalarsClient {
    private var retrofit: Retrofit? = null
    @JvmStatic
    fun getScalarClient(baseUrl: String?): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        }
        return retrofit
    }
}