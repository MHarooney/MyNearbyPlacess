package com.example.chamatestapp;

import com.example.chamatestapp.Remote.IGoogleAPIService;
import com.example.chamatestapp.Remote.RetrofitClient;

public class Common {

    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";
    public static IGoogleAPIService getGoogleAPIService()
    {
        return RetrofitClient.INSTANCE.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
