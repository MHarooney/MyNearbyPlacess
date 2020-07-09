package com.example.chamatestapp;

import com.example.chamatestapp.Model.Place;
import com.example.chamatestapp.Remote.IGoogleAPIService;
import com.example.chamatestapp.Remote.RetrofitClient;
import com.example.chamatestapp.Remote.RetrofitScalarsClient;

public class Common {

    public static Place currentResult;

    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";
    public static IGoogleAPIService getGoogleAPIService()
    {
        return RetrofitClient.INSTANCE.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
    public static IGoogleAPIService getGoogleAPIServiceScalars()
    {
        return RetrofitScalarsClient.getScalarClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
